package com.xmartlabs.template.controller

import android.support.annotation.CheckResult
import com.xmartlabs.bigbang.core.controller.Controller
import com.xmartlabs.template.model.Session
import com.xmartlabs.template.service.AuthService
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

open class AuthController @Inject constructor(
    private val authService: AuthService,
    private val sessionController: SessionController
) : Controller() {

  //TODO: Change signature and method to match authService request to fetch the Access Token
  val accessToken: Single<out Session>
    @CheckResult
    get() = authService.accessToken
        .applyIoSchedulers()
        .filter { authResponse -> authResponse.accessToken != null }
        .toSingle()
        .map { Session(it.accessToken) }
        .doOnSuccess { sessionController.session = it }

  open fun signIn() = Completable.error(NotImplementedError())
}
