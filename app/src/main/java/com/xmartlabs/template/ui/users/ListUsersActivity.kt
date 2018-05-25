package com.xmartlabs.template.ui.users

import com.f2prateek.dart.HensonNavigable
import com.xmartlabs.bigbang.ui.SingleFragmentActivity
import com.xmartlabs.template.ui.recyclerfragmentexample.RecyclerExampleFragmentBuilder

@HensonNavigable
class ListUsersActivity : SingleFragmentActivity() {
  override fun createFragment() = RecyclerExampleFragmentBuilder().build()
}
