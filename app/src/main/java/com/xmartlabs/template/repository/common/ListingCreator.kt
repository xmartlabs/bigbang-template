package com.xmartlabs.template.repository.common

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder

/**
 * Created by mirland on 28/05/18.
 */
object ListingCreator {
  fun <T> createListing(pageFetcher: PageFetcher<T>, pageSize: Int): Listing<T> {
    val sourceFactory = ServicePagedDataSourceFactory(pageFetcher)
    val livePagedList = LivePagedListBuilder(sourceFactory, pageSize)
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
