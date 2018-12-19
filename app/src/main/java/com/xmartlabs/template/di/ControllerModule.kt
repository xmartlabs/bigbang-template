package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.controller.CoreSessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.template.repository.SessionRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {
  @Provides
  @Singleton
  internal fun provideSessionController(sharedPreferencesController: SharedPreferencesController)
      = SessionRepository(sharedPreferencesController)

  @Provides
  @Singleton
  internal fun provideCoreSessionController(sessionRepository: SessionRepository): CoreSessionController
      = sessionRepository
}
