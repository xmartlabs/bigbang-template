package com.xmartlabs.template.repository.common

import com.xmartlabs.template.model.service.ListResponse
import io.reactivex.Single


interface PageFetcher<T> {
  fun getPage(page: Int, pageSize: Int): Single<ListResponse<T>>
}