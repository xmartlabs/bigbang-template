package com.xmartlabs.template.ui.users

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.xmartlabs.template.repository.user.UserRepository
import javax.inject.Inject

class ListUsersViewModel @Inject constructor(userRepository: UserRepository) : ViewModel(){
  private val userName = MutableLiveData<String>()
  private val repoResult = Transformations.map(userName, {
    userRepository.searchServiceUsers(it, 30)
  })
  val posts = Transformations.switchMap(repoResult, { it.pagedList })!!
  val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
  val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!

  fun refresh() {
    repoResult.value?.refresh?.invoke()
  }

  fun showUsers(username: String): Boolean {
    if (userName.value == username) {
      return false
    }
    userName.value = username
    return true
  }

  fun retry() {
    val listing = repoResult?.value
    listing?.retry?.invoke()
  }
}
