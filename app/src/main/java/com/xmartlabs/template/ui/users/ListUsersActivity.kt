package com.xmartlabs.template.ui.users

import com.f2prateek.dart.HensonNavigable
import com.xmartlabs.bigbang.ui.SingleFragmentActivity

@HensonNavigable
class ListUsersActivity : SingleFragmentActivity() {
  override fun createFragment() = ListUsersFragmentBuilder().build()
}
