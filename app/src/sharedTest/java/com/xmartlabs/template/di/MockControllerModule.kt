package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.controller.CoreSessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.template.repository.AuthController
import com.xmartlabs.template.repository.MockAuthController
import com.xmartlabs.template.repository.SessionController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockControllerModule {
  @Provides
  @Singleton
  internal fun provideAuthController(mockAuthController: MockAuthController): AuthController = mockAuthController

  @Provides
  @Singleton
  internal fun provideCoreSessionController(sessionController: SessionController) : CoreSessionController = sessionController

  @Provides
  @Singleton
  internal fun provideSessionController(sharedPreferencesController: SharedPreferencesController) = SessionController(sharedPreferencesController)
}
