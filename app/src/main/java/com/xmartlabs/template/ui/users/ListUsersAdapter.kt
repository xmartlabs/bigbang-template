package com.xmartlabs.template.ui.users

import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xmartlabs.template.R
import com.xmartlabs.template.databinding.ItemUserBinding
import com.xmartlabs.template.model.User
import com.xmartlabs.template.repository.common.NetworkState

class ListUsersAdapter(private val retryCallback: () -> Unit)
  : PagedListAdapter<User, RecyclerView.ViewHolder>(USER_COMPARATOR) {
  private var networkState: NetworkState? = null
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      R.layout.item_user -> UserViewHolder.create(parent)
      R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
      else -> throw IllegalArgumentException("unknown view type $viewType")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (getItemViewType(position)) {
      R.layout.item_user -> (holder as UserViewHolder).bind(getItem(position))
      R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bind(networkState)
    }
  }

  private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

  override fun getItemViewType(position: Int): Int {
    return if (hasExtraRow() && position == itemCount - 1) {
      R.layout.network_state_item
    } else {
      R.layout.item_user
    }
  }

  override fun getItemCount(): Int {
    return super.getItemCount() + if (hasExtraRow()) 1 else 0
  }

  fun setNetworkState(newNetworkState: NetworkState?) {
    val previousState = this.networkState
    val hadExtraRow = hasExtraRow()
    this.networkState = newNetworkState
    val hasExtraRow = hasExtraRow()
    if (hadExtraRow != hasExtraRow) {
      if (hadExtraRow) {
        notifyItemRemoved(super.getItemCount())
      } else {
        notifyItemInserted(super.getItemCount())
      }
    } else if (hasExtraRow && previousState != newNetworkState) {
      notifyItemChanged(itemCount - 1)
    }
  }

  companion object {
    val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
      override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
          oldItem == newItem

      override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
          oldItem.name == newItem.name
    }
  }

  class UserViewHolder(val binding: ItemUserBinding)
    : RecyclerView.ViewHolder(binding.root) {
    companion object {
      fun create(parent: ViewGroup): UserViewHolder {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false
        )
        return UserViewHolder(binding)
      }
    }

    fun bind(user: User?) {
      binding.user = user
    }
  }
}
