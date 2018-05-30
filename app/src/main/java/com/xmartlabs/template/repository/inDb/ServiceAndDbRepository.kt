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

class ServiceAndDbRepository<T, ServiceResponse>(
    private val pageFetcher: PageFetcher<ServiceResponse>,
    private val databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
    private val db: RoomDatabase,
    private val ioExecutor: Executor,
    private val pagedListConfig: PagedList.Config,
    private val firstPage: Int) {

  companion object {
    private const val DEFAULT_NETWORK_PAGE_SIZE = 30

    fun <Value, ServiceResponse> createListing(
        dataSourceFactory: DataSource.Factory<*, Value>,
        db: RoomDatabase,
        pageFetcher: PageFetcher<ServiceResponse>,
        databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
        ioExecutor: Executor? = null,
        firstPage: Int = 1,
        pagedListConfig: PagedList.Config = PagedList.Config.Builder().setPageSize(DEFAULT_NETWORK_PAGE_SIZE).build()
    ): Listing<Value> {

      val repository = ServiceAndDbRepository<Value, ServiceResponse>(
          pageFetcher = pageFetcher,
          databaseQueryHandler = databaseQueryHandler,
          ioExecutor = Executors.newFixedThreadPool(5),
          pagedListConfig = pagedListConfig,
          firstPage = firstPage,
          db = db
      )

      val boundaryCallback = ServiceAndDatabaseBoundaryCallback<Value, ServiceResponse>(
          pageFetcher = pageFetcher,
          db = db,
          databaseQueryHandler = databaseQueryHandler,
          pagedListConfig = pagedListConfig,
          ioExecutor = repository.ioExecutor
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

  fun runInTransaction(body: Runnable)
}
