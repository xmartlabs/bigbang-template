package com.xmartlabs.template.di

import com.xmartlabs.template.service.AuthService
import com.xmartlabs.template.service.UserService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RestServiceModuleApi {
  @Provides
  @Singleton
  internal fun provideAuthService(retrofit: Retrofit) = retrofit.create(AuthService::class.java)

  @Provides
  @Singleton
  internal fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)
}
