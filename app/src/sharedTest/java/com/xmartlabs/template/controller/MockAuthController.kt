package com.xmartlabs.template.controller

import com.xmartlabs.template.repository.AuthRepository
import com.xmartlabs.template.repository.SessionRepository
import com.xmartlabs.template.service.AuthService
import io.reactivex.Completable
import javax.inject.Inject

class MockAuthController @Inject constructor(authService: AuthService, sessionRepository: SessionRepository)
  : AuthRepository(authService, sessionRepository) {
  override fun signIn() = Completable.complete()
}
