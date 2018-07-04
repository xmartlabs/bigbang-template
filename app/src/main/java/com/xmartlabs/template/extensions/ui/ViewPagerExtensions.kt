@file:Suppress("NOTHING_TO_INLINE")

package com.xmartlabs.template.extensions.ui

import android.support.v4.view.ViewPager
import com.xmartlabs.template.helper.EmptyOnPageChangeListener

inline fun ViewPager.nextPage() {
  currentItem++
}

inline fun ViewPager.isLastPage() = currentItem == (adapter?.let { adapter -> adapter.count - 1 })

inline fun ViewPager.onPageSelected(crossinline action: (position: Int) -> Unit) {
  addOnPageChangeListener(object : EmptyOnPageChangeListener() {
    override fun onPageSelected(position: Int) = action.invoke(position)
  })
}
