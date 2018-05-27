package com.xmartlabs.template.di

import com.xmartlabs.template.ui.login.LoginFragment
import com.xmartlabs.template.ui.onboarding.OnboardingFragment
import com.xmartlabs.template.ui.onboarding.page.OnboardingPageFragment
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExampleFragment
import com.xmartlabs.template.ui.users.ListUsersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributeListUsersFragment(): ListUsersFragment

  @ContributesAndroidInjector
  abstract fun contributeLoginFragment(): LoginFragment

  @ContributesAndroidInjector
  abstract fun contributeOnboardingFragment(): OnboardingFragment

  @ContributesAndroidInjector
  abstract fun contributeOnboardingPageFragment(): OnboardingPageFragment

  @ContributesAndroidInjector
  abstract fun contributeRecyclerExampleFragment(): RecyclerExampleFragment
}
