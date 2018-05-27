package com.xmartlabs.template.di

import com.xmartlabs.template.ui.StartActivity
import com.xmartlabs.template.ui.login.LoginActivity
import com.xmartlabs.template.ui.onboarding.OnboardingActivity
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExampleActivity
import com.xmartlabs.template.ui.users.ListUsersActivity
import com.xmartlabs.template.ui.users.ListUsersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeListUsersActivity(): ListUsersActivity

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeLoginActivity(): LoginActivity

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeOnboardingActivity(): OnboardingActivity

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeRecyclerExampleActivity(): RecyclerExampleActivity

  @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
  abstract fun contributeStartActivity(): StartActivity
}
