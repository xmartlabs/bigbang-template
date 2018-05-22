package com.xmartlabs.template.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.xmartlabs.bigbang.ui.BaseFragment
import com.xmartlabs.template.App
import com.xmartlabs.template.R
import com.xmartlabs.template.extensions.ui.hasInvisibleAlpha
import com.xmartlabs.template.extensions.ui.isLastPage
import com.xmartlabs.template.extensions.ui.nextPage
import com.xmartlabs.template.extensions.ui.onPageSelected
import com.xmartlabs.template.extensions.ui.setAnimatedVisibility
import com.xmartlabs.template.helper.EmptyOnPageChangeListener
import com.xmartlabs.template.ui.Henson
import kotlinx.android.synthetic.main.fragment_onboarding.*

@FragmentWithArgs
class OnboardingFragment : BaseFragment() {
  companion object {
    private const val ALPHA_START_DELAY_MILLISECONDS: Long = 100
  }

  override val layoutResId = R.layout.fragment_onboarding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewPager.adapter = OnboardingPageAdapter(childFragmentManager)

    listOf(startButton, skipButton)
        .forEach { it.setOnClickListener { _ -> goToLoginActivity() } }

    nextButton.setOnClickListener {_ -> viewPager.nextPage() }
    viewPager.onPageSelected { _ -> handleNextButtonVisibility() }
  }

  private fun handleNextButtonVisibility() {
    if (viewPager.isLastPage()) {
      swapView(nextButton, startButton)
    } else if (nextButton.hasInvisibleAlpha() || nextButton.visibility == View.INVISIBLE) {
      swapView(startButton, nextButton)
    }
  }

  private fun swapView(fromView: View, toView: View) {
    fromView.visibility = View.INVISIBLE
    toView.setAnimatedVisibility(true, ALPHA_START_DELAY_MILLISECONDS)
  }

  fun goToLoginActivity() {
    val intent = Henson.with(App.context)
        .gotoLoginActivity()
        .build()
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
  }
}
