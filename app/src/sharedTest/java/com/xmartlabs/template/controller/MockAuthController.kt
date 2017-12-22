package com.xmartlabs.template.controller

import io.reactivex.Completable

class MockAuthController : AuthController() {
  override fun signIn() = Completable.complete()
}
