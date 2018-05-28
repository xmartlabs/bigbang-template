package com.xmartlabs.template.ui.users

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xmartlabs.template.R
import com.xmartlabs.template.databinding.NetworkStateItemBinding
import com.xmartlabs.template.repository.common.NetworkState

/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(private val binding: NetworkStateItemBinding,
                                 private val retryCallback: () -> Unit)
  : RecyclerView.ViewHolder(binding.root) {
  companion object {
    fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
      val binding = DataBindingUtil.inflate<NetworkStateItemBinding>(
          LayoutInflater.from(parent.context),
          R.layout.network_state_item,
          parent,
          false
      )
      return NetworkStateItemViewHolder(binding, retryCallback)
    }
  }

  init {
    binding.retryButton.setOnClickListener {
      retryCallback()
    }
  }

  fun bind(networkState: NetworkState?) {
    binding.networkState = networkState
  }
}