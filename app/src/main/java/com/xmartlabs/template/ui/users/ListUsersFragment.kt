package com.xmartlabs.template.ui.users

import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.xmartlabs.bigbang.ui.BaseFragment
import com.xmartlabs.template.R
import com.xmartlabs.template.model.User
import com.xmartlabs.template.repository.common.NetworkState
import kotlinx.android.synthetic.main.fragment_list_users.*
import javax.inject.Inject

@FragmentWithArgs
class ListUsersFragment : BaseFragment() {
  @Inject
  lateinit var model: ListUsersViewModel

  @LayoutRes
  override val layoutResId = R.layout.fragment_list_users

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initAdapter()
    initSwipeToRefresh()
    initSearch()
  }

  private fun initAdapter() {
    val adapter = ListUsersAdapter { model.retry() }
    list.adapter = adapter
    model.posts.observe(this, Observer<PagedList<User>> {
      adapter.submitList(it)
    })
    model.networkState.observe(this, Observer {
      adapter.setNetworkState(it)
    })
  }

  private fun initSwipeToRefresh() {
    model.refreshState.observe(this, Observer {
      swipe_refresh.isRefreshing = it == NetworkState.LOADING
    })
    swipe_refresh.setOnRefreshListener {
      model.refresh()
    }
  }

  private fun initSearch() {
    input.setOnEditorActionListener({ _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_GO) {
        updatedUsernameFromInput()
        true
      } else {
        false
      }
    })
    input.setOnKeyListener({ _, keyCode, event ->
      if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
        updatedUsernameFromInput()
        true
      } else {
        false
      }
    })
  }

  private fun updatedUsernameFromInput() {
    input.text.trim().toString().let {
      if (it.isNotEmpty()) {
        if (model.showUsers(it)) {
          list.scrollToPosition(0)
          (list.adapter as? ListUsersAdapter)?.submitList(null)
        }
      }
    }
  }
}