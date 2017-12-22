package com.xmartlabs.template

import com.xmartlabs.bigbang.core.controller.SessionController
import com.xmartlabs.bigbang.core.controller.SharedPreferencesController
import com.xmartlabs.bigbang.core.module.CoreAndroidModule
import com.xmartlabs.bigbang.core.module.GsonModule
import com.xmartlabs.bigbang.core.module.OkHttpModule
import com.xmartlabs.bigbang.core.module.PicassoModule
import com.xmartlabs.bigbang.core.module.SessionInterceptor
import com.xmartlabs.bigbang.core.providers.AccessTokenProvider
import com.xmartlabs.bigbang.retrofit.module.RestServiceModule
import com.xmartlabs.bigbang.retrofit.module.ServiceGsonModule
import com.xmartlabs.template.controller.AuthController
import com.xmartlabs.template.module.ControllerModule
import com.xmartlabs.template.module.RestServiceModuleApi
import com.xmartlabs.template.ui.login.LoginActivity
import com.xmartlabs.template.ui.login.LoginFragment
import com.xmartlabs.template.ui.login.LoginPresenter
import com.xmartlabs.template.ui.onboarding.OnboardingActivity
import com.xmartlabs.template.ui.onboarding.OnboardingFragment
import com.xmartlabs.template.ui.onboarding.OnboardingPresenter
import com.xmartlabs.template.ui.onboarding.page.OnboardingPageFragment
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExampleActivity
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExampleFragment
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExamplePresenter
import com.xmartlabs.template.ui.StartActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
  ControllerModule::class,
  CoreAndroidModule::class,
  GsonModule::class,
  OkHttpModule::class,
  PicassoModule::class,
  RestServiceModule::class,
  RestServiceModuleApi::class,
  ServiceGsonModule::class
])
@Singleton
@SuppressWarnings("TooManyFunctions")
interface ApplicationComponent {
  fun inject(app: App)

  fun inject(loginActivity: LoginActivity)
  fun inject(onboardingActivity: OnboardingActivity)
  fun inject(recyclerExampleActivity: RecyclerExampleActivity)
  fun inject(startActivity: StartActivity)

  fun inject(loginFragment: LoginFragment)
  fun inject(onboardingFragment: OnboardingFragment)
  fun inject(onboardingPageFragment: OnboardingPageFragment)
  fun inject(recyclerExampleFragment: RecyclerExampleFragment)

  fun inject(loginPresenter: LoginPresenter)
  fun inject(onboardingPresenter: OnboardingPresenter)
  fun inject(recyclerExamplePresenter: RecyclerExamplePresenter)

  fun inject(authController: AuthController)
  fun inject(sessionController: SessionController)
  fun inject(sharedPreferencesController: SharedPreferencesController)

  fun inject(accessTokenProvider: AccessTokenProvider)
  fun inject(sessionInterceptor: SessionInterceptor)
}
