package com.xmartlabs.template.controller.user

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.xmartlabs.template.service.NetworkState
import com.xmartlabs.template.model.User
import com.xmartlabs.template.service.UserService
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 * See ItemKeyedSubredditDataSource
 */
class PageKeyedSubredditDataSource(
    private val userService: UserService,
    private val userName: String
) : PageKeyedDataSource<Int, User>() {

  // keep a function reference for the retry event
  private var retry: (() -> Any)? = null

  /**
   * There is no sync on the state because paging will always call loadInitial first then wait
   * for it to return some success value before calling loadAfter.
   */
  val networkState = MutableLiveData<NetworkState>()

  val initialLoad = MutableLiveData<NetworkState>()

  fun retryAllFailed() {
    val prevRetry = retry
    retry = null
    prevRetry?.let {
      /*retryExecutor.execute {
          it.invoke()
      }*/
    }
  }

  override fun loadBefore(
      params: LoadParams<Int>,
      callback: LoadCallback<Int, User>) {
    // ignored, since we only ever append to our initial load
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    networkState.postValue(NetworkState.LOADING)
    userService.searchUsers(name = userName, page = params.key)
        .subscribe(object : SingleObserver<List<User>> {
          override fun onSuccess(data: List<User>) {
            retry = null
            callback.onResult(data, params.key + 1)
            networkState.postValue(NetworkState.LOADED)
          }

          override fun onSubscribe(d: Disposable) {

          }

          override fun onError(t: Throwable) {
            retry = {
              loadAfter(params, callback)
            }
            networkState.postValue(NetworkState.error(t.message ?: "unknown err"))

/*
            networkState.postValue(
                NetworkState.error("error code: ${response.code()}"))
*/
          }
        })
  }

  override fun loadInitial(
      params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, User>) {
    networkState.postValue(NetworkState.LOADING)
    initialLoad.postValue(NetworkState.LOADING)


    userService.searchUsers(
        name = userName,
        page = 1
    )
        .subscribe(object : SingleObserver<List<User>> {
          override fun onSuccess(data: List<User>) {
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(data, -1, 2)
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