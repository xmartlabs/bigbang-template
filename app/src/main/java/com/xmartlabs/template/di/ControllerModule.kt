package com.xmartlabs.template.di

import com.xmartlabs.bigbang.core.controller.CoreSessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.template.controller.SessionController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ControllerModule {
  @Provides
  @Singleton
  internal fun provideSessionController(sharedPreferencesController: SharedPreferencesController)
      = SessionController(sharedPreferencesController)

  @Provides
  @Singleton
  internal fun provideCoreSessionController(sessionController: SessionController): CoreSessionController
      = sessionController
}
