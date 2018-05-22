package com.xmartlabs.template.di

import android.app.Application
import com.xmartlabs.bigbang.core.model.BuildInfo  as CoreBuildInfo

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.xmartlabs.template.model.common.BuildInfo

@Module
class AppModule {
  @Provides
  @Singleton
  fun provideBuildInformation(coreBuildInfo : BuildInfo) : CoreBuildInfo = coreBuildInfo
}
