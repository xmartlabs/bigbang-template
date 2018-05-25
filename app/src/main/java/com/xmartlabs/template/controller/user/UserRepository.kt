package com.xmartlabs.template.controller.user

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.support.annotation.MainThread
import com.xmartlabs.template.model.User
import com.xmartlabs.template.service.Listing
import com.xmartlabs.template.service.UserService
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userService: UserService) {
  @MainThread
  fun getUsersListing(userName: String, pageSize: Int): Listing<User> {
    val sourceFactory = UserDataSourceFactory(userService = userService, userName = userName)
    val livePagedList = LivePagedListBuilder(sourceFactory, pageSize)
        // provide custom executor for network requests, otherwise it will default to
        // Arch Components' IO pool which is also used for disk access
//        .setFetchExecutor(networkExecutor)
        .build()

    val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
      it.initialLoad
    }
    return Listing(
        pagedList = livePagedList,
        networkState = Transformations.switchMap(sourceFactory.sourceLiveData, {
          it.networkState
        }),
        retry = {
          sourceFactory.sourceLiveData.value?.retryAllFailed()
        },
        refresh = {
          sourceFactory.sourceLiveData.value?.invalidate()
        },
        refreshState = refreshState
    )
  }
}
