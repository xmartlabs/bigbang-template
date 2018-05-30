package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import android.arch.paging.PagingRequestHelper
import android.support.annotation.MainThread
import com.xmartlabs.bigbang.core.extensions.observeOnIo
import com.xmartlabs.bigbang.core.extensions.subscribeOnIo
import com.xmartlabs.template.model.service.ListResponse
import com.xmartlabs.template.repository.common.NetworkState
import com.xmartlabs.template.repository.common.PageFetcher
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.util.concurrent.Executor

class ServiceAndDatabaseBoundaryCallback<T>(private val pageFetcher: PageFetcher<T>,
                                            private val handleResponse: (ListResponse<T>?) -> Unit,
                                            private val networkPageSize: Int,
                                            private val ioExecutor: Executor) : PagedList.BoundaryCallback<T>() {

  var page = 1
  val helper = PagingRequestHelper(ioExecutor)
  val networkState = helper.createStatusLiveData()

  /**
   * Database returned 0 items. We should query the backend for more items.
   */
  @MainThread
  override fun onZeroItemsLoaded() {
    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
      pageFetcher.getPage(page = page, pageSize = networkPageSize * 3)
          .createWebserviceCallback(it)
    }
  }

  /**
   * User reached to the end of the list.
   */
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


  fun Single<ListResponse<T>>.createWebserviceCallback(callback: PagingRequestHelper.Request.Callback) {
    this.subscribeOnIo()
        .observeOnIo()
        .subscribe(object : SingleObserver<ListResponse<T>> {
          override fun onSuccess(data: ListResponse<T>) {
            page++
            handleResponse(data)
            callback.recordSuccess()
          }

          override fun onSubscribe(d: Disposable) {

          }

          override fun onError(t: Throwable) {
            callback.recordFailure(t)
          }
        })
  }

  private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
      report.getErrorFor(it)?.message
    }.first()
  }

  fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
      when {
        report.hasRunning() -> liveData.postValue(NetworkState.LOADING)
        report.hasError() -> liveData.postValue(
            NetworkState.error(getErrorMessage(report)))
        else -> liveData.postValue(NetworkState.LOADED)
      }
    }
    return liveData
  }
}
