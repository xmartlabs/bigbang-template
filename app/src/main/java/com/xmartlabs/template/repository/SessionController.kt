package com.xmartlabs.template.repository

import com.xmartlabs.bigbang.core.controller.CoreSessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.template.model.Session
import javax.inject.Inject

class SessionController @Inject constructor(sharedPreferencesController: SharedPreferencesController)
  : CoreSessionController(sharedPreferencesController) {
  override fun getSessionType() = Session::class.java

  var session
    get() = abstractSession as? Session?
    set(value) { value?.let { saveSession(it) } ?: deleteSession() }

  fun update(block: (Session?) -> Session) { session = block(session) }
}
