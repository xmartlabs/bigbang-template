package com.xmartlabs.template

import com.xmartlabs.template.di.ApplicationComponent
import com.xmartlabs.template.di.MockRestServiceModule
import com.xmartlabs.template.model.common.BuildInfo

class TestApplication : App() {
  override fun createComponent(): ApplicationComponent = DaggerInstrumentalTestComponent.builder()
      .application(this)
      .buildInfo(BuildInfo())
      .restServiceModule(MockRestServiceModule())
      .build()
}
