package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.persistence.room.RoomDatabase
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.xmartlabs.bigbang.core.extensions.observeOnIo
import com.xmartlabs.bigbang.core.extensions.subscribeOnIo
import com.xmartlabs.template.model.service.ListResponse
import com.xmartlabs.template.repository.common.Listing
import com.xmartlabs.template.repository.common.NetworkState
import com.xmartlabs.template.repository.common.PageFetcher
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Repository implementation that uses a database PagedList + a boundary callback to return a
 * listing that loads in pages.
 */
class ServiceAndDbRepository<T>(
    private val pageFetcher: PageFetcher<T>,
    private val databaseFunctionsHandler: DatabaseFunctionsHandler<T>,
    private val db: RoomDatabase,
    private val ioExecutor: Executor,
    private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE) {

  companion object {
    private const val DEFAULT_NETWORK_PAGE_SIZE = 10

    fun <Key, Value> createListing(
        dataSourceFactory: DataSource.Factory<Key, Value>,
        db: RoomDatabase,
        pageFetcher: PageFetcher<Value>,
        databaseFunctionsHandler: DatabaseFunctionsHandler<Value>,
        ioExecutor: Executor? = null,
        networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE): Listing<Value> {

      val repository = ServiceAndDbRepository(
          pageFetcher = pageFetcher,
          databaseFunctionsHandler = databaseFunctionsHandler,
          ioExecutor = Executors.newFixedThreadPool(5),
          db = db
      )

      val boundaryCallback = ServiceAndDatabaseBoundaryCallback(
          pageFetcher = pageFetcher,
          db = db,
          databaseFunctionsHandler = databaseFunctionsHandler,
          networkPageSize = repository.networkPageSize,
          ioExecutor = repository.ioExecutor
      )

      val builder = LivePagedListBuilder(dataSourceFactory, repository.networkPageSize)
          .setBoundaryCallback(boundaryCallback)

      val refreshTrigger = MutableLiveData<Unit>()
      val refreshState = Transformations.switchMap(refreshTrigger, {
        repository.refresh()
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

  @MainThread
  private fun refresh(): LiveData<NetworkState> {
    val networkState = MutableLiveData<NetworkState>()
    networkState.value = NetworkState.LOADING
    pageFetcher.getPage(page = 1, pageSize = networkPageSize)
        .subscribeOnIo()
        .observeOnIo()
        .subscribe(object : SingleObserver<ListResponse<T>> {
          override fun onSuccess(t: ListResponse<T>) {
            db.runInTransaction {
              databaseFunctionsHandler.deleteEntities()
              databaseFunctionsHandler.saveEntities(t)
            }

            networkState.postValue(NetworkState.LOADED)
          }

          override fun onSubscribe(d: Disposable) {}

          override fun onError(e: Throwable) {
            networkState.postValue(NetworkState.error(e.message))
          }
        })
    return networkState
  }
}

interface DatabaseFunctionsHandler<T> {
  @WorkerThread
  fun saveEntities(listResponse: ListResponse<T>?)

  @WorkerThread
  fun deleteEntities()
}
