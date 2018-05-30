package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.module.OkHttpLoggingInterceptorKey
import com.xmartlabs.bigbang.core.module.SessionInterceptor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton
import com.xmartlabs.bigbang.core.module.OkHttpModule as CoreOkHttpModule
import com.facebook.stetho.okhttp3.StethoInterceptor



@Module
class OkHttpModule : CoreOkHttpModule() {
  @Named(SESSION_INTERCEPTOR)
  @Provides
  @Singleton
  fun provideSessionInterceptor(sessionInterceptor: SessionInterceptor): Interceptor = sessionInterceptor

  @IntoMap
  @Provides
  @OkHttpLoggingInterceptorKey(StethoInterceptor::class)
  @Singleton
  fun provideStethoInterceptor(): Interceptor = StethoInterceptor()
}
