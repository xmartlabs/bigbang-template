package com.xmartlabs.template.tests.signin

import com.xmartlabs.template.App
import com.xmartlabs.template.R
import com.xmartlabs.template.common.BaseUnitTest
import com.xmartlabs.template.controller.AuthController
import com.xmartlabs.template.service.AuthService
import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.RequestsVerifier
import io.appflate.restmock.utils.RequestMatchers
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert
import org.junit.Test
import javax.inject.Inject

class SignInUnitTest : BaseUnitTest() {
  companion object {
    private val POST_ACCESS_TOKEN_MATCHER =
        allOf(RequestMatchers.isPOST(), RequestMatchers.pathContains(AuthService.URL_ACCESS_TOKEN))
  }

  @Inject
  internal lateinit var authController: AuthController

  override fun setUp() {
    super.setUp()
    testComponent.inject(this)
  }

  @Test
  fun signInTest() {
    authController.signIn().blockingAwait()
  }

  @Test
  fun testAccessTokenOk() {
    RESTMockServer.whenRequested(POST_ACCESS_TOKEN_MATCHER)
        .thenReturn(MockResponse().setResponseCode(411))

    authController.accessToken
        .ignoreElement()
        .onErrorComplete()
        .blockingAwait()

    RequestsVerifier.verifyRequest(POST_ACCESS_TOKEN_MATCHER).invoked()
  }

  @Test
  fun testMockString() {
    Assert.assertEquals(DEFAULT_MOCK_STRING, App.context.getString(R.string.app_name))
  }
}
