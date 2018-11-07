package com.xmartlabs.template

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.test.espresso.IdlingRegistry
import android.support.test.runner.AndroidJUnitRunner
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.xmartlabs.bigbang.core.module.OkHttpModule
import com.xmartlabs.template.common.ImmediateNewThreadScheduler
import io.appflate.restmock.RESTMockServerStarter
import io.appflate.restmock.android.AndroidAssetsFileParser
import io.appflate.restmock.android.AndroidLogger
import io.reactivex.plugins.RxJavaPlugins
import okhttp3.OkHttpClient
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class TestRunner : AndroidJUnitRunner() {
  companion object {
    val picassoClientIdleName = String.format(Locale.US, "%sOkHttp", OkHttpModule.CLIENT_PICASSO)
    val serviceClientIdleName = String.format(Locale.US, "%sOkHttp", OkHttpModule.CLIENT_SERVICE)

    @JvmStatic
    var androidAssetsFileParser: AndroidAssetsFileParser? = null
      private set
    @JvmStatic
    var testContext: Context? = null
      private set
  }

  @Inject
  @field:[Named (OkHttpModule.CLIENT_PICASSO)]
  internal lateinit var picassoOkHttpClient: OkHttpClient
  @Inject
  @field:[Named (OkHttpModule.CLIENT_SERVICE)]
  internal lateinit var serviceOkHttpClient: OkHttpClient
  @Inject
  lateinit var mockedTimeZone: TimeZone

  override fun onCreate(arguments: Bundle) {
    super.onCreate(arguments)

    RxJavaPlugins.setIoSchedulerHandler { ImmediateNewThreadScheduler() }
  }

  @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
  override fun newApplication(classLoader: ClassLoader, className: String, context: Context): Application {
    testContext = getContext()
    androidAssetsFileParser = AndroidAssetsFileParser(testContext)
    RESTMockServerStarter.startSync(androidAssetsFileParser, AndroidLogger())

    return super.newApplication(classLoader, TestApplication::class.java.name, context)
  }

  override fun callApplicationOnCreate(app: Application) {
    super.callApplicationOnCreate(app)
    ((app as TestApplication).applicationComponent as InstrumentalTestComponent)
        .inject(this)
    TimeZone.setDefault(mockedTimeZone)

    val picassoIdlingResource = OkHttp3IdlingResource.create(picassoClientIdleName, picassoOkHttpClient)
    IdlingRegistry.getInstance().register(picassoIdlingResource)

    val serviceIdlingResource = OkHttp3IdlingResource.create(serviceClientIdleName, serviceOkHttpClient)
    IdlingRegistry.getInstance().register(serviceIdlingResource)
  }
}

