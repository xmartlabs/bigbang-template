package com.xmartlabs.template.uitest.sign

import com.xmartlabs.bigbang.test.extensions.checkHasText
import com.xmartlabs.bigbang.test.extensions.checkIsDisplayed
import com.xmartlabs.bigbang.test.helpers.EspressoUtils
import com.xmartlabs.template.R
import com.xmartlabs.template.common.SingleActivityInstrumentationTest
import com.xmartlabs.template.ui.login.LoginActivity
import org.junit.Test

class LoginInstrumentalTest : SingleActivityInstrumentationTest<LoginActivity>() {
  override val activityClass: Class<LoginActivity>
    get() = LoginActivity::class.java

  @Test
  fun testSignInButtonIsVisible() {
    launchActivityWithDefaultIntent()
    EspressoUtils.onViewWithId(R.id.loginButton)
        .checkIsDisplayed()
  }

  @Test
  fun testSignInButtonText() {
    launchActivityWithDefaultIntent()
    EspressoUtils.onViewWithId(R.id.loginButton)
        .checkHasText(R.string.login)
  }
}
