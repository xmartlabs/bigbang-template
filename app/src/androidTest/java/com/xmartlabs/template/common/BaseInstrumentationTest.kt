package com.xmartlabs.template.common

import android.app.Activity
import android.app.Instrumentation
import android.support.annotation.CallSuper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.xmartlabs.bigbang.core.Injector
import com.xmartlabs.bigbang.test.extensions.getFirstActivityInstance
import io.appflate.restmock.RESTMockServer
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentationTest {
  lateinit var instrumentation: Instrumentation

  @Before
  @CallSuper
  fun setUp() {
    instrumentation = InstrumentationRegistry.getInstrumentation()
    Injector.inject(this)
    RESTMockServer.reset()
  }

  val activityInstance: Activity?
    get() = instrumentation.getFirstActivityInstance()
}
