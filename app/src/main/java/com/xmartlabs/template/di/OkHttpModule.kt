package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.module.SessionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Named
import javax.inject.Singleton
import com.xmartlabs.bigbang.core.module.OkHttpModule as CoreOkHttpModule

@Module
class OkHttpModule : CoreOkHttpModule() {
  @Named(SESSION_INTERCEPTOR)
  @Provides
  @Singleton
  fun provideSessionInterceptor(sessionInterceptor: SessionInterceptor): Interceptor = sessionInterceptor
}
