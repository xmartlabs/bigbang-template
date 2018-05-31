package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.room.RoomDatabase
import android.support.annotation.WorkerThread
import com.xmartlabs.template.repository.common.Listing
import com.xmartlabs.template.repository.common.PageFetcher
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ServiceAndDbRepository {
  companion object {
    private const val DEFAULT_NETWORK_PAGE_SIZE = 30

    fun <Value, ServiceResponse> createListing(
        dataSourceFactory: DataSource.Factory<*, Value>,
        pageFetcher: PageFetcher<ServiceResponse>,
        databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
        ioServiceExecutor: Executor = Executors.newFixedThreadPool(5),
        ioDatabaseExecutor: Executor = Executors.newSingleThreadExecutor(),
        firstPage: Int = 1,
        pagedListConfig: PagedList.Config = PagedList.Config.Builder().setPageSize(DEFAULT_NETWORK_PAGE_SIZE).build()
    ): Listing<Value> {

      val boundaryCallback = BoundaryCallback<Value, ServiceResponse>(
          pageFetcher = pageFetcher,
          firstPage = firstPage,
          databaseQueryHandler = databaseQueryHandler,
          pagedListConfig = pagedListConfig,
          ioDatabaseExecutor = ioDatabaseExecutor,
          ioServiceExecutor = ioServiceExecutor
      )

      val builder = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
          .setBoundaryCallback(boundaryCallback)

      val refreshTrigger = MutableLiveData<Unit>()
      val refreshState = Transformations.switchMap(refreshTrigger, {
        boundaryCallback.resetData()
      })

      return Listing(
          pagedList = builder.build(),
          networkState = boundaryCallback.networkState,
          retry = {
            boundaryCallback.helper.retryAllFailed()
          },
          refresh = {
            refreshTrigger.value = null
          },
          refreshState = refreshState
      )
    }
  }
}

interface DatabaseQueryHandler<T> {
  @WorkerThread
  fun saveEntities(response: T?)

  @WorkerThread
  fun dropEntities()

  @WorkerThread
  fun runInTransaction(transaction: () -> Unit)
}
