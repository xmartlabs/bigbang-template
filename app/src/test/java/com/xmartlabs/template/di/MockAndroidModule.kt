package com.xmartlabs.template.di

import android.content.Context
import android.content.SharedPreferences
import com.xmartlabs.bigbang.core.model.BuildInfo
import com.xmartlabs.template.model.common.BuildInfo as CoreBuildInfo
import com.xmartlabs.template.App
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockAndroidModule {
  companion object {
    internal val MOCK_CONTEXT = Mockito.mock(App::class.java)
  }

  @Provides
  @Singleton
  fun provideBuildInformation(): BuildInfo = CoreBuildInfo()

  @Provides
  @Singleton
  fun provideApplicationContext(app: App): Context = app

  @Provides
  @Singleton
  fun provideApplication(): App = MOCK_CONTEXT

  @Provides
  @Singleton
  fun provideSharedPreferences(): SharedPreferences = Mockito.mock(SharedPreferences::class.java)
}
