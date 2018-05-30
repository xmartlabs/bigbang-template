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
                                                             private val networkPageSize: Int,
                                                             private val ioExecutor: Executor) : PagedList.BoundaryCallback<T>() {

  var page = 1
  val helper = PagingRequestHelper(ioExecutor)
  val networkState = helper.createStatusLiveData()

  init {
    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      pageFetcher.getPage(page = page, pageSize = networkPageSize * 3)
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
      pageFetcher.getPage(page = page, pageSize = networkPageSize)
          .createWebserviceCallback(it)
    }
  }

  override fun onItemAtFrontLoaded(itemAtFront: T) {
    // ignored, since we only ever append to what's in the DB
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

  private fun getError(report: PagingRequestHelper.StatusReport): Throwable = PagingRequestHelper.RequestType.values()
      .mapNotNull { report.getErrorFor(it) }
      .first()

  private fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
      when {
        report.hasRunning() -> liveData.postValue(NetworkState.LOADING)
        report.hasError() -> liveData.postValue(
            NetworkState.error(getError(report)))
        else -> liveData.postValue(NetworkState.LOADED)
      }
    }
    return liveData
  }
}
