package com.xmartlabs.template.repository

import com.xmartlabs.template.service.AuthService
import io.reactivex.Completable
import javax.inject.Inject

class MockAuthController @Inject constructor(authService: AuthService, sessionController: SessionController)
  : AuthController(authService, sessionController) {
  override fun signIn() = Completable.complete()
}
