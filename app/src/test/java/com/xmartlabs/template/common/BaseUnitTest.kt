package com.xmartlabs.template.common

import com.xmartlabs.bigbang.core.Injector
import com.xmartlabs.template.App
import com.xmartlabs.template.di.MockAndroidModule
import com.xmartlabs.template.di.MockRestServiceModule
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidLocalFileParser
import io.appflate.restmock.android.AndroidLogger
import org.junit.Before
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject

open class BaseUnitTest {
  companion object {
    const val DEFAULT_MOCK_STRING = "This is a mock string"
  }

  @Inject
  lateinit var mockedTimeZone: TimeZone

  open fun mockStrings() {
    `when`(App.context.getString(ArgumentMatchers.anyInt())).thenReturn(DEFAULT_MOCK_STRING)
  }

  open fun mockTime() {
    TimeZone.setDefault(mockedTimeZone)
  }

  @Before
  fun setUp() {
    App.context = MockAndroidModule.MOCK_CONTEXT

    RESTMockServerStarter.startSync(AndroidLocalFileParser(App.context), AndroidLogger())
    val applicationComponent = DaggerTestComponent.builder()
        .restServiceModule(MockRestServiceModule())
        .build()

    Injector.instance.bullet = BulletTestComponent(applicationComponent)
    Injector.inject(this)

    mockStrings()
    mockTime()
  }
}
