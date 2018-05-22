package com.xmartlabs.template.service

import com.xmartlabs.template.model.Column

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by chaca on 5/10/17.
 */
interface ProjectService {
  companion object {
    const val BASE_URL = "projects"
    const val PROJECT_ID = "project_id"
    const val GET_COLUMNS = "$BASE_URL/{$PROJECT_ID}/columns"
  }

  @GET(GET_COLUMNS)
  fun getAllColumns(@Path(PROJECT_ID) projectId: Long?): Single<List<Column>>
}
