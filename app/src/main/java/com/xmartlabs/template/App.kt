package com.xmartlabs.template

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.support.annotation.VisibleForTesting
import android.support.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tspoon.traceur.Traceur
import com.tspoon.traceur.TraceurConfig
import com.xmartlabs.bigbang.core.di.AppInjector
import com.xmartlabs.bigbang.core.helper.GeneralErrorHelper
import com.xmartlabs.bigbang.core.log.LoggerTree
import com.xmartlabs.bigbang.log.crashlytics.CrashlyticsLogger
import com.xmartlabs.bigbang.retrofit.helper.ServiceErrorHandler
import com.xmartlabs.template.di.ApplicationComponent
import com.xmartlabs.template.di.DaggerApplicationComponent
import com.xmartlabs.template.di.OkHttpModule
import com.xmartlabs.template.di.RestServiceModule
import com.xmartlabs.template.di.ServiceGsonModule
import com.xmartlabs.template.model.common.BuildInfo
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

open class App : Application(), HasActivityInjector {
  companion object {
    @Suppress("LateinitUsage")
    @JvmStatic
    lateinit var context: App
      @VisibleForTesting internal set
  }

  @Inject
  internal lateinit var buildInfo: BuildInfo
  @Inject
  internal lateinit var generalErrorHelper: GeneralErrorHelper
  @Inject
  internal lateinit var loggerTree: LoggerTree
  @Inject
  internal lateinit var serviceErrorHandler: ServiceErrorHandler
  @Inject
  internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
  @Suppress("LateinitUsage")
  internal lateinit var applicationComponent: ApplicationComponent

  init {
    @Suppress("LeakingThis")
    context = this
  }

  override fun activityInjector() = dispatchingAndroidInjector

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !BuildConfig.DEBUG) {
      MultiDex.install(this)
    }
  }

  override fun onCreate() {
    super.onCreate()
    initializeThreeTenABP()
    initializeInjections()
    initializeRxErrorHandler()
    initializeLogging() // Crashlytics initialization should go at the end.
  }

  private fun initializeInjections() {
    applicationComponent = createComponent()
    applicationComponent.inject(this)
    AppInjector.init(this)
  }

  @VisibleForTesting
  protected open fun createComponent(): ApplicationComponent = DaggerApplicationComponent.builder()
      .application(this)
      .buildInfo(BuildInfo())
      .okHttpModule(OkHttpModule())
      .restServiceGsonModule(ServiceGsonModule())
      .restServiceModule(RestServiceModule())
      .build()

  private fun initializeThreeTenABP() = AndroidThreeTen.init(this)

  private fun initializeLogging() {
    //TODO: Configure Fabric and add Fabric apiSecret and apiKey properties file in the root folder
    loggerTree.addLogger(CrashlyticsLogger().initialize(buildInfo, this))
    Timber.plant(loggerTree)

    if (buildInfo.isDebug) {
      Stetho.initializeWithDefaults(this)
    }
  }

  private fun initializeRxErrorHandler() {
    serviceErrorHandler.handleServiceErrors()

    val level = if (BuildConfig.DEBUG) Traceur.AssemblyLogLevel.SHOW_ALL else Traceur.AssemblyLogLevel.NONE
    val config = TraceurConfig(true, level) { generalErrorHelper.generalErrorAction(it) }
    Traceur.enableLogging(config)
  }
}
