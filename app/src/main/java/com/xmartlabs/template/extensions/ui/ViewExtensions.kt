package com.xmartlabs.template.extensions.ui

import android.view.View

private const val ALPHA_INVISIBLE = 0.01f
private const val ALPHA_OPAQUE = 1.0f

fun View.setAnimatedVisibility(visible: Boolean, startDelay: Long = 0) {
  @Suppress("ComplexCondition")
  if (visible && visibility == View.VISIBLE || !visible && visibility != View.VISIBLE) {
    return
  }
  visibility = View.VISIBLE

  animate()
      .alpha(if (visible) ALPHA_OPAQUE else ALPHA_INVISIBLE)
      .setStartDelay(startDelay)
      .withEndAction({ visibility = if (visible) View.VISIBLE else View.INVISIBLE })
      .start()
}

fun View.hasInvisibleAlpha(): Boolean = alpha <= ALPHA_INVISIBLE