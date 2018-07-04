package com.xmartlabs.template

import android.app.Application
import com.xmartlabs.bigbang.core.module.AndroidModule
import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.common.BaseInstrumentationTest
import com.xmartlabs.template.di.ActivityModule
import com.xmartlabs.template.di.AppModule
import com.xmartlabs.template.di.ApplicationComponent
import com.xmartlabs.template.di.MockClockModule
import com.xmartlabs.template.di.MockControllerModule
import com.xmartlabs.template.di.OkHttpModule
import com.xmartlabs.template.di.RestServiceModuleApi
import com.xmartlabs.template.di.ViewModelModule
import com.xmartlabs.template.model.common.BuildInfo
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  ActivityModule::class,
  AndroidInjectionModule::class,
  AndroidModule::class,
  AppModule::class,
  GsonModule::class,
  MockClockModule::class,
  MockControllerModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class,
  ViewModelModule::class,
  ServiceGsonModule::class
])
interface InstrumentalTestComponent : ApplicationComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    @BindsInstance
    fun buildInfo(buildInfo: BuildInfo): Builder

    fun restServiceModule(restService: RestServiceModule): Builder

    fun build(): InstrumentalTestComponent
  }

  fun inject(testRunner: TestRunner)

  fun inject(baseInstrumentationTest: BaseInstrumentationTest)
}
