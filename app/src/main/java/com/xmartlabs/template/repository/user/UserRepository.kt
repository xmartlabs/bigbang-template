package com.xmartlabs.template.repository.user

import android.arch.persistence.room.RoomDatabase
import android.support.annotation.MainThread
import com.android.example.github.db.UserDao
import com.xmartlabs.template.model.User
import com.xmartlabs.template.model.UserSearch
import com.xmartlabs.template.model.service.ListResponse
import com.xmartlabs.template.repository.common.ListResponsePageFetcher
import com.xmartlabs.template.repository.common.Listing
import com.xmartlabs.template.repository.common.ListingCreator
import com.xmartlabs.template.repository.common.PageFetcher
import com.xmartlabs.template.repository.inDb.DatabaseQueryHandler
import com.xmartlabs.template.repository.inDb.ServiceAndDbRepository
import com.xmartlabs.template.service.UserService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    private val db: RoomDatabase
) {
  @MainThread
  fun searchServiceUsers(userName: String, pageSize: Int): Listing<User> {
    val pageFetcher: ListResponsePageFetcher<User> = (object : ListResponsePageFetcher<User> {
      override fun getPage(page: Int, pageSize: Int): Single<ListResponse<User>> =
          userService.searchUsers(userName, page = page, pageSize = pageSize)
    })
    return ListingCreator.createListing(pageFetcher, pageSize)
  }

  @MainThread
  fun searchServiceAndDbUsers(userName: String, pageSize: Int): Listing<User> {
    val pageFetcher: PageFetcher<ListResponse<User>> = (object : PageFetcher<ListResponse<User>> {
      override fun getPage(page: Int, pageSize: Int) =
          userService.searchUsers(userName, page = page, pageSize = pageSize)
    })

    val databaseFunctionsHandler = object : DatabaseQueryHandler<ListResponse<User>> {
      override fun runInTransaction(body: Runnable) {
        db.runInTransaction(body)
      }

      override fun saveEntities(listResponse: ListResponse<User>?) {
        listResponse?.items?.let { users ->
          val start = userDao.getNextIndexInUserSearch(userName)
          val relationItems = users
              .mapIndexed { index, user ->
                UserSearch(search = userName, userId = user.id, searchPosition = start + index)
              }
          userDao.insert(users)
          userDao.insertUserSearch(relationItems)
        }
      }

      override fun dropEntities() {
        userDao.deleteUserSearch(userName)
      }
    }
    return ServiceAndDbRepository.createListing(
        pageFetcher = pageFetcher,
        databaseQueryHandler = databaseFunctionsHandler,
        db = db,
        dataSourceFactory = userDao.findUsersByName(userName)
    )
  }
}
