package com.xmartlabs.template

import com.xmartlabs.bigbang.core.module.CoreAndroidModule
import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.OkHttpModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.common.BaseInstrumentationTest
import com.xmartlabs.template.module.MockClockModule
import com.xmartlabs.template.module.MockControllerModule
import com.xmartlabs.template.module.RestServiceModuleApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
  CoreAndroidModule::class,
  MockControllerModule::class,
  MockClockModule::class,
  GsonModule::class,
  ServiceGsonModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class
])
interface InstrumentalTestComponent : ApplicationComponent {
  fun inject(testRunner: TestRunner)

  fun inject(baseInstrumentationTest: BaseInstrumentationTest)
}
