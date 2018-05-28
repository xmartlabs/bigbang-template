package com.xmartlabs.template.di

import com.xmartlabs.template.model.common.BuildInfo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.xmartlabs.bigbang.core.model.BuildInfo as CoreBuildInfo

@Module
class AppModule {
  @Provides
  @Singleton
  fun provideBuildInformation(coreBuildInfo : BuildInfo) : CoreBuildInfo = coreBuildInfo
}
