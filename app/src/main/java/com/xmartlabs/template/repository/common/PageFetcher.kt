package com.xmartlabs.template.repository.common

import android.support.annotation.CheckResult
import com.xmartlabs.template.model.service.ListResponse
import io.reactivex.Single


interface PageFetcher<T> {
  @CheckResult
  fun getPage(page: Int, pageSize: Int): Single<ListResponse<T>>
}