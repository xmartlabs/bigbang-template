package com.xmartlabs.template.service

import com.xmartlabs.template.model.AffiliationType
import com.xmartlabs.template.model.Organization
import com.xmartlabs.template.model.Repository
import com.xmartlabs.template.model.User

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by chaca on 5/8/17.
 */
interface UserService {
  companion object {
    const val BASE_URL = "user"

    const val AFFILIATION_QUERY_FIELD = "affiliation"

    const val GET_USER = BASE_URL
    const val GET_USER_ORGANIZATIONS = "$BASE_URL/orgs"
    const val GET_REPOSITORIES = "$BASE_URL/repos"
    const val GET_SEARCH_USERS = "/search/users"
  }

  @GET(GET_USER)
  fun searchUsers(@Query("q") name: String, @Query("q") page: Int): Single<List<User>>

  @GET(GET_USER)
  fun getUser(): Single<User>

  @GET(GET_USER_ORGANIZATIONS)
  fun getOrganizations(): Single<List<Organization>>

  @GET(GET_REPOSITORIES)
  fun getRepositories(@Query(AFFILIATION_QUERY_FIELD) type: AffiliationType): Single<List<Repository>>
}
