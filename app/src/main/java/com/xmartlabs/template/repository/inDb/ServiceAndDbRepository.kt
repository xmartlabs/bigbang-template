package com.xmartlabs.template.repository.inDb

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.persistence.room.RoomDatabase
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.xmartlabs.template.repository.common.Listing
import com.xmartlabs.template.repository.common.NetworkState
import com.xmartlabs.template.repository.common.PageFetcher
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ServiceAndDbRepository<T, ServiceResponse>(
    private val pageFetcher: PageFetcher<ServiceResponse>,
    private val databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
    private val db: RoomDatabase,
    private val ioExecutor: Executor,
    private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE) {

  companion object {
    private const val DEFAULT_NETWORK_PAGE_SIZE = 10

    fun <Value, ServiceResponse> createListing(
        dataSourceFactory: DataSource.Factory<*, Value>,
        db: RoomDatabase,
        pageFetcher: PageFetcher<ServiceResponse>,
        databaseQueryHandler: DatabaseQueryHandler<ServiceResponse>,
        ioExecutor: Executor? = null,
        networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE): Listing<Value> {

      val repository = ServiceAndDbRepository<Value, ServiceResponse>(
          pageFetcher = pageFetcher,
          databaseQueryHandler = databaseQueryHandler,
          ioExecutor = Executors.newFixedThreadPool(5),
          networkPageSize = networkPageSize,
          db = db
      )

      val boundaryCallback = ServiceAndDatabaseBoundaryCallback<Value, ServiceResponse>(
          pageFetcher = pageFetcher,
          db = db,
          databaseQueryHandler = databaseQueryHandler,
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
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(object : SingleObserver<ServiceResponse> {
          override fun onSuccess(t: ServiceResponse) {
            db.runInTransaction {
              databaseQueryHandler.dropEntities()
              databaseQueryHandler.saveEntities(t)
            }

            networkState.postValue(NetworkState.LOADED)
          }

          override fun onSubscribe(d: Disposable) {}

          override fun onError(e: Throwable) {
            networkState.postValue(NetworkState.error(e))
          }
        })
    return networkState
  }
}

interface DatabaseQueryHandler<T> {
  @WorkerThread
  fun saveEntities(response: T?)

  @WorkerThread
  fun dropEntities()

  fun runInTransaction(body: Runnable)
}
