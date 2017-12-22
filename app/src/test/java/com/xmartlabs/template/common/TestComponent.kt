package com.xmartlabs.template.common

import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.OkHttpModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.ApplicationComponent
import com.xmartlabs.template.module.MockAndroidModule
import com.xmartlabs.template.module.MockClockModule
import com.xmartlabs.template.module.MockControllerModule
import com.xmartlabs.template.module.RestServiceModuleApi
import com.xmartlabs.template.tests.signin.SignInUnitTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
  GsonModule::class,
  MockAndroidModule::class,
  MockClockModule::class,
  MockControllerModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class,
  ServiceGsonModule::class
])
interface TestComponent : ApplicationComponent {
  fun inject(baseUnitTest: BaseUnitTest)

  fun inject(signInUnitTest: SignInUnitTest)
}
