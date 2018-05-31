package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.arch.paging.PagingRequestHelper
import android.arch.persistence.room.RoomDatabase
import android.support.annotation.AnyThread
import android.support.annotation.MainThread
import com.xmartlabs.template.repository.common.NetworkState
import com.xmartlabs.template.repository.common.PageFetcher
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class BoundaryCallback<T, ServiceResponse>(private val pageFetcher: PageFetcher<ServiceResponse>,
                                           private val databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
                                           private val pagedListConfig: PagedList.Config,
                                           private val ioServiceExecutor: Executor,
                                           private val ioDatabaseExecutor: Executor,
                                           private val firstPage: Int
) : PagedList.BoundaryCallback<T>() {
  var page = firstPage
  var helper = PagingRequestHelper(ioServiceExecutor)
  val networkState = MutableLiveData<NetworkState>()
  val networkStateListener: (PagingRequestHelper.StatusReport) -> Unit = { report ->
    networkState.postValue(report.createNetworkState())
  }

  init {
    helper.addListener(networkStateListener)
    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      pageFetcher.getPage(page = page, pageSize = pagedListConfig.initialLoadSizeHint)
          .createWebserviceCallback(it, true)
    }
  }

  @MainThread
  override fun onZeroItemsLoaded() {}

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

  @AnyThread
  fun resetData(): LiveData<NetworkState> {
    val networkState = MutableLiveData<NetworkState>()
    networkState.postValue(NetworkState.LOADING)
    pageFetcher.getPage(page = firstPage, pageSize = pagedListConfig.initialLoadSizeHint)
        .subscribeOn(ioServiceExecutor)
        .observeOn(ioDatabaseExecutor)
        .subscribe(object : SingleObserver<ServiceResponse> {
          override fun onSuccess(t: ServiceResponse) {
            unsubscribePendingRequests()
            page = firstPage + 1
            databaseQueryHandler.runInTransaction {
              databaseQueryHandler.dropEntities()
              databaseQueryHandler.saveEntities(t)
            }
            helper.removeListener(networkStateListener)
            helper = PagingRequestHelper(ioServiceExecutor)
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

  @AnyThread
  private fun <T> Single<T>.subscribeOn(executor: Executor) = this.subscribeOn(Schedulers.from(executor))

  @AnyThread
  private fun <T> Single<T>.observeOn(executor: Executor) = this.observeOn(Schedulers.from(executor))


  private fun Single<ServiceResponse>.createWebserviceCallback(callback: PagingRequestHelper.Request.Callback,
                                                               dropDatabase: Boolean = false) {
    this
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(object : SingleObserver<ServiceResponse> {
          override fun onSuccess(data: ServiceResponse) {
            page++
            databaseQueryHandler.runInTransaction {
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
