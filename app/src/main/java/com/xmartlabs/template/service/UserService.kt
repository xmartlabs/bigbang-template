package com.xmartlabs.template.service

import com.xmartlabs.template.model.AffiliationType
import com.xmartlabs.template.model.Organization
import com.xmartlabs.template.model.Repository
import com.xmartlabs.template.model.User
import com.xmartlabs.template.model.service.GhListResult

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by chaca on 5/8/17.
 */
interface UserService {
  companion object {
    const val GET_SEARCH_USERS = "/search/users"
  }

  @GET(GET_SEARCH_USERS)
  fun searchUsers(@Query("q") name: String,
                  @Query("page") page: Int,
                  @Query("per_page") pageSize: Int
  ): Single<GhListResult<User>>
}
