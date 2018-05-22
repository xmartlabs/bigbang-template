package com.xmartlabs.template

import com.xmartlabs.template.di.AppModule
import com.xmartlabs.template.di.ApplicationComponent
import com.xmartlabs.template.di.MockRestServiceModule

class TestApplication : App() {
  override fun createComponent(): ApplicationComponent = DaggerInstrumentalTestComponent.builder()
        .coreAndroidModule(AppModule(this))
        .restServiceModule(MockRestServiceModule())
        .build()

  override fun createBullet(component: ApplicationComponent) =
      BulletInstrumentalTestComponent(component as InstrumentalTestComponent)
}
