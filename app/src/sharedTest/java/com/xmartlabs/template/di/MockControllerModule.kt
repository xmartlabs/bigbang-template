package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.controller.CoreSessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.template.repository.AuthRepository
import com.xmartlabs.template.repository.MockAuthController
import com.xmartlabs.template.repository.SessionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockControllerModule {
  @Provides
  @Singleton
  internal fun provideAuthController(mockAuthController: MockAuthController): AuthRepository = mockAuthController

  @Provides
  @Singleton
  internal fun provideCoreSessionController(sessionRepository: SessionRepository) : CoreSessionController = sessionRepository

  @Provides
  @Singleton
  internal fun provideSessionController(sharedPreferencesController: SharedPreferencesController) = SessionRepository(sharedPreferencesController)
}
