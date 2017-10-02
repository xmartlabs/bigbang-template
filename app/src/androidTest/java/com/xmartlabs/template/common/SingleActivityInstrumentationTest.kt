package com.xmartlabs.template.common

import android.app.Activity
import android.content.Intent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.rule.ActivityTestRule
import com.xmartlabs.bigbang.core.extensions.ignoreException
import com.xmartlabs.bigbang.test.extensions.performClick
import com.xmartlabs.bigbang.test.helpers.EspressoUtils
import org.junit.Rule
import org.junit.Test

/**
 * Created by mirland on 10/6/17.
 */
abstract class SingleActivityInstrumentationTest<T : Activity> : BaseInstrumentationTest() {
  @Rule
  @JvmField
  var activityTestRule = createTestRule()

  protected val defaultIntent: Intent? = null

  protected fun createTestRule(): ActivityTestRule<T> = IntentsTestRule(activityClass, true, false)

  @Test
  fun checkActivityOpens() {
    launchActivityWithDefaultIntent()
  }

  @Test
  fun checkUpNavigation() {
    launchActivityWithDefaultIntent()
    EspressoUtils.onUpButtonView().ignoreException { performClick() }
  }

  protected fun launchActivityWithDefaultIntent() {
    activityTestRule.launchActivity(defaultIntent)
  }

  protected abstract val activityClass: Class<T>
}

