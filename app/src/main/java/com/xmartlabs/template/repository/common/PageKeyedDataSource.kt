package com.xmartlabs.template.repository.common

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.xmartlabs.bigbang.core.extensions.subscribeOnIo
import com.xmartlabs.template.model.service.ListResponse
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class PageKeyedDataSource<T>(private val pageFetcher: PageFetcher<T>) : PageKeyedDataSource<Int, T>() {
  private var retry: (() -> Any)? = null
  val networkState = MutableLiveData<NetworkState>()
  val initialLoad = MutableLiveData<NetworkState>()

  fun retryAllFailed() {
    val prevRetry = retry
    retry = null
    prevRetry?.invoke()
  }

  override fun loadBefore(
      params: LoadParams<Int>,
      callback: LoadCallback<Int, T>) {
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    networkState.postValue(NetworkState.LOADING)
    pageFetcher.getPage(page = params.key, pageSize = params.requestedLoadSize)
        .subscribeOnIo()
        .subscribe(object : SingleObserver<ListResponse<T>> {
          override fun onSuccess(data: ListResponse<T>) {
            retry = null
            callback.onResult(data.items, params.key + 1)
            networkState.postValue(NetworkState.LOADED)
          }

          override fun onSubscribe(d: Disposable) {

          }

          override fun onError(t: Throwable) {
            retry = {
              loadAfter(params, callback)
            }
            networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
          }
        })
  }

  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
    networkState.postValue(NetworkState.LOADING)
    initialLoad.postValue(NetworkState.LOADING)
    pageFetcher.getPage(page = 1, pageSize = params.requestedLoadSize)
        .subscribeOnIo()
        .subscribe(object : SingleObserver<ListResponse<T>> {
          override fun onSuccess(data: ListResponse<T>) {
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(data.items, -1, 2)
          }

          override fun onSubscribe(d: Disposable) {

          }

          override fun onError(t: Throwable) {
            retry = {
              loadInitial(params, callback)
            }
            val error = NetworkState.error(t.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
          }
        })
  }
}
