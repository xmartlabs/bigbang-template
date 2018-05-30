package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.arch.paging.PagingRequestHelper
import android.arch.persistence.room.RoomDatabase
import android.support.annotation.MainThread
import com.xmartlabs.template.repository.common.NetworkState
import com.xmartlabs.template.repository.common.PageFetcher
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class ServiceAndDatabaseBoundaryCallback<T, ServiceResponse>(private val pageFetcher: PageFetcher<ServiceResponse>,
                                                             private val db: RoomDatabase,
                                                             private val databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
                                                             private val pagedListConfig: PagedList.Config,
                                                             private val ioExecutor: Executor,
                                                             private val firstPage: Int = 1
) : PagedList.BoundaryCallback<T>() {

  var page = firstPage
  var helper = PagingRequestHelper(ioExecutor)
  val networkState = MutableLiveData<NetworkState>()
  val networkStateListener: (PagingRequestHelper.StatusReport) -> Unit = {
    report -> networkState.postValue(report.createNetworkState())
  }

  init {
    helper.addListener(networkStateListener)
    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      pageFetcher.getPage(page = page, pageSize = pagedListConfig.initialLoadSizeHint)
          .createWebserviceCallback(it, true)
    }
  }

  @MainThread
  override fun onZeroItemsLoaded() {
//    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
//      pageFetcher.getPage(page = page, pageSize = networkPageSize * 3)
//          .createWebserviceCallback(it)
//    }
  }

  @MainThread
  override fun onItemAtEndLoaded(itemAtEnd: T) {
    helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
      pageFetcher.getPage(page = page, pageSize = pagedListConfig.pageSize)
          .createWebserviceCallback(it)
    }
  }

  override fun onItemAtFrontLoaded(itemAtFront: T) {
    // ignored, since we only ever append to what's in the DB
  }


  fun resetData(): LiveData<NetworkState> {
    val networkState = MutableLiveData<NetworkState>()
    networkState.value = NetworkState.LOADING
    pageFetcher.getPage(page = firstPage, pageSize = pagedListConfig.initialLoadSizeHint)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(object : SingleObserver<ServiceResponse> {
          override fun onSuccess(t: ServiceResponse) {
            unsubscribePendingRequests();
            db.runInTransaction {
              databaseQueryHandler.dropEntities()
              databaseQueryHandler.saveEntities(t)
            }
            helper.removeListener(networkStateListener)
            helper = PagingRequestHelper(ioExecutor)
            helper.addListener(networkStateListener)
            networkState.postValue(NetworkState.LOADED)
          }

          override fun onSubscribe(d: Disposable) {}

          override fun onError(e: Throwable) {
            networkState.postValue(NetworkState.error(e))
          }
        })
    return networkState
  }

  private fun unsubscribePendingRequests() {

  }

  private fun Single<ServiceResponse>.createWebserviceCallback(callback: PagingRequestHelper.Request.Callback,
                                                               dropDatabase: Boolean = false) {
    this
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(object : SingleObserver<ServiceResponse> {
          override fun onSuccess(data: ServiceResponse) {
            page++
            db.runInTransaction {
              if (dropDatabase) {
                databaseQueryHandler.dropEntities()
              }
              databaseQueryHandler.saveEntities(data)
            }
            callback.recordSuccess()
          }

          override fun onSubscribe(d: Disposable) {

          }

          override fun onError(t: Throwable) {
            callback.recordFailure(t)
          }
        })
  }

  private fun PagingRequestHelper.StatusReport.getError(): Throwable = PagingRequestHelper.RequestType.values()
      .mapNotNull { getErrorFor(it) }
      .first()

  private fun PagingRequestHelper.StatusReport.createNetworkState()
      : NetworkState {
    return when {
      hasRunning() -> NetworkState.LOADING
      hasError() -> NetworkState.error(getError())
      else -> NetworkState.LOADED
    }
  }
}
