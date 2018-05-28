package com.xmartlabs.template.di

import android.content.Context
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import io.appflate.restmock.RESTMockServer
import okhttp3.HttpUrl

class MockRestServiceModule : RestServiceModule() {
  @Suppress("UnsafeCallOnNullableType")
  override fun provideBaseUrl(context: Context): HttpUrl = HttpUrl.parse(RESTMockServer.getUrl())!!
}
