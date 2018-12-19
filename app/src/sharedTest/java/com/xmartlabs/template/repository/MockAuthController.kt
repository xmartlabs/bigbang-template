package com.xmartlabs.template.repository

import com.xmartlabs.template.service.AuthService
import io.reactivex.Completable
import javax.inject.Inject

class MockAuthController @Inject constructor(authService: AuthService, sessionRepository: SessionRepository)
  : AuthRepository(authService, sessionRepository) {
  override fun signIn() = Completable.complete()
}
