package com.xmartlabs.template.extensions.ui

import android.support.v4.view.ViewPager
import com.xmartlabs.template.helper.EmptyOnPageChangeListener

fun ViewPager.nextPage() {
  currentItem++
}

fun ViewPager.isLastPage() = currentItem == (adapter?.let { adapter -> adapter.count - 1 })

inline fun ViewPager.onPageSelected(crossinline action: (position: Int) -> Unit) {
  addOnPageChangeListener(object : EmptyOnPageChangeListener() {
    override fun onPageSelected(position: Int) = action.invoke(position)
  })
}
