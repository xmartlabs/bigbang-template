package com.xmartlabs.template

import com.xmartlabs.template.module.AndroidModule
import com.xmartlabs.template.module.MockRestServiceModule

class TestApplication : App() {
  override fun createComponent(): ApplicationComponent = DaggerInstrumentalTestComponent.builder()
        .coreAndroidModule(AndroidModule(this))
        .restServiceModule(MockRestServiceModule())
        .build()

  override fun createBullet(component: ApplicationComponent) =
      BulletInstrumentalTestComponent(component as InstrumentalTestComponent)
}
