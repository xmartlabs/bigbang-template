package com.xmartlabs.template.di

import android.app.Application
import com.xmartlabs.bigbang.core.module.AndroidModule
import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.App
import com.xmartlabs.template.model.common.BuildInfo
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [
  ActivityModule::class,
  AndroidInjectionModule::class,
  AndroidModule::class,
  AppModule::class,
  ControllerModule::class,
  GsonModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class,
  ViewModelModule::class,
  ServiceGsonModule::class
])
@Singleton
interface ApplicationComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    @BindsInstance
    fun buildInfo(buildInfo: BuildInfo): Builder

    fun restServiceModule(restService: RestServiceModule): Builder

    fun okHttpModule(okHttpModule: OkHttpModule): Builder

    fun build(): ApplicationComponent
  }

  fun inject(app: App)
}
