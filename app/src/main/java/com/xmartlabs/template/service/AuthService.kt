package com.xmartlabs.template.service

import com.xmartlabs.template.model.service.AuthResponse

import io.reactivex.Single
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
  companion object {
    const val URL_ACCESS_TOKEN = "login/oauth/access_token"

    const val QUERY_CLIENT_ID = "client_id"
    const val QUERY_CLIENT_SECRET = "client_secret"
    const val QUERY_CODE = "code"
    const val QUERY_STATE = "state"
  }

  @Headers("Accept: application/json")
  @POST(URL_ACCESS_TOKEN)
  fun getAccessToken(@Query(QUERY_CLIENT_ID) clientId: String,
                     @Query(QUERY_CLIENT_SECRET) clientSecret: String,
                     @Query(QUERY_CODE) code: String,
                     @Query(QUERY_STATE) state: String): Single<AuthResponse>
}
