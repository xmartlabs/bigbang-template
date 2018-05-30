package com.xmartlabs.template.repository.common

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource

class ServicePagedDataSourceFactory<T>(private val pageFetcher: ListResponsePageFetcher<T>)
    : DataSource.Factory<Int, T>() {
    val sourceLiveData = MutableLiveData<PageKeyedDataSource<T>>()
    override fun create(): DataSource<Int, T> {
        val source = PageKeyedDataSource(pageFetcher)
        sourceLiveData.postValue(source)
        return source
    }
}
