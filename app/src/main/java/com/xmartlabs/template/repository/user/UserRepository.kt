package com.xmartlabs.template.repository.user

import android.support.annotation.MainThread
import com.xmartlabs.template.model.User
import com.xmartlabs.template.model.service.ListResponse
import com.xmartlabs.template.repository.common.ListingCreator
import com.xmartlabs.template.repository.common.PageFetcher
import com.xmartlabs.template.service.Listing
import com.xmartlabs.template.service.UserService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userService: UserService) {
  @MainThread
  fun getUsersListing(userName: String, pageSize: Int): Listing<User> {
    val pageFetcher: PageFetcher<User> = (object : PageFetcher<User> {
      override fun getPage(page: Int, pageSize: Int): Single<ListResponse<User>> =
        userService.searchUsers(userName, page = page, pageSize = pageSize)
    })
    return ListingCreator.createListing(pageFetcher, pageSize)
  }
}
