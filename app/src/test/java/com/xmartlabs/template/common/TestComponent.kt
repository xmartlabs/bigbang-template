package com.xmartlabs.template.common

import android.app.Application
import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.di.ActivityModule
import com.xmartlabs.template.di.AppModule
import com.xmartlabs.template.di.ApplicationComponent
import com.xmartlabs.template.di.MockAndroidModule
import com.xmartlabs.template.di.MockClockModule
import com.xmartlabs.template.di.MockControllerModule
import com.xmartlabs.template.di.OkHttpModule
import com.xmartlabs.template.di.RestServiceModuleApi
import com.xmartlabs.template.di.ViewModelModule
import com.xmartlabs.template.model.common.BuildInfo
import com.xmartlabs.template.tests.signin.SignInUnitTest
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  AppModule::class,
  ActivityModule::class,
  AndroidInjectionModule::class,
  GsonModule::class,
  MockAndroidModule::class,
  MockClockModule::class,
  MockControllerModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class,
  ViewModelModule::class,
  ServiceGsonModule::class
])
interface TestComponent : ApplicationComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    @BindsInstance
    fun buildInfo(buildInfo: BuildInfo): Builder

    fun restServiceGsonModule(serviceGsonModule: ServiceGsonModule): Builder

    fun restServiceModule(restService: RestServiceModule): Builder

    fun okHttpModule(okHttpModule: OkHttpModule): Builder

    fun build(): TestComponent
  }

  fun inject(signInUnitTest: SignInUnitTest)
}
