package com.xmartlabs.template.extensions.ui

import android.view.View

private const val ALPHA_INVISIBLE = 0.01f
private const val ALPHA_OPAQUE = 1.0f

fun View.setAnimatedVisibility(visible: Boolean, startDelay: Long = 0) {
  @Suppress("ComplexCondition")
  if (visible && alpha == ALPHA_OPAQUE || !visible && alpha == ALPHA_INVISIBLE) {
    return
  }
  visibility = View.VISIBLE

  animate()
      .alpha(if (visible) ALPHA_OPAQUE else ALPHA_INVISIBLE)
      .setStartDelay(startDelay)
      .start()
}

fun View.hasInvisibleAlpha(): Boolean = alpha <= ALPHA_INVISIBLE