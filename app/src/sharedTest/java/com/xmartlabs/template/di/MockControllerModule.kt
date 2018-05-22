package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.controller.SessionController
import com.xmartlabs.template.controller.AuthController
import com.xmartlabs.template.controller.MockAuthController
import com.xmartlabs.template.model.Session
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockControllerModule {
  @Provides
  @Singleton
  internal fun provideAuthController(): AuthController = MockAuthController()

  @Provides
  @Singleton
  internal fun provideSessionController() = SessionController(Session::class.java)
}
