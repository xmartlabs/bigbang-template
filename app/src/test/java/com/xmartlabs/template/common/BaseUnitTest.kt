package com.xmartlabs.template.common

import com.xmartlabs.template.App
import com.xmartlabs.template.di.DaggerApplicationComponent
import com.xmartlabs.template.di.MockAndroidModule
import com.xmartlabs.template.di.MockClockModule
import com.xmartlabs.template.di.MockRestServiceModule
import com.xmartlabs.template.model.common.BuildInfo
import com.xmartlabs.template.tests.signin.SignInUnitTest
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidLocalFileParser
import io.appflate.restmock.android.AndroidLogger
import org.junit.Before
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject

open class BaseUnitTest {
  companion object {
    const val DEFAULT_MOCK_STRING = "This is a mock string"
  }

  internal lateinit var testComponent: TestComponent

  open fun mockStrings() {
    `when`(App.context.getString(ArgumentMatchers.anyInt())).thenReturn(DEFAULT_MOCK_STRING)
  }

  open fun mockTime() {
    TimeZone.setDefault(MockClockModule.DEFAULT_TIME_ZONE)
  }

  @Before
  open fun setUp() {
    App.context = MockAndroidModule.MOCK_CONTEXT

    MockitoAnnotations.initMocks(this)

    RESTMockServerStarter.startSync(AndroidLocalFileParser(App.context), AndroidLogger())
    testComponent = DaggerTestComponent.builder()
        .application(App.context)
        .buildInfo(BuildInfo())
        .restServiceModule(MockRestServiceModule())
        .build()

    mockStrings()
    mockTime()
  }
}
