package com.xmartlabs.template.di

import android.content.Context
import com.xmartlabs.template.App
import com.xmartlabs.template.R
import okhttp3.HttpUrl
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule as CoreRestServiceModule

class RestServiceModule : CoreRestServiceModule() {
  companion object {
    private val BASE_URL = App.context.resources.getString(R.string.base_url)
  }

  @Suppress("UnsafeCallOnNullableType")
  override fun provideBaseUrl(context: Context): HttpUrl = HttpUrl.parse(BASE_URL)!!
}
