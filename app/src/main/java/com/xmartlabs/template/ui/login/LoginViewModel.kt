package com.xmartlabs.template.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.android.example.paging.pagingwithnetwork.reddit.repository.NetworkState
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {
  private val _login = MutableLiveData<Pair<String, String>>()

  val login: LiveData<NetworkState>
    get() =
      Transformations.switchMap(_login) { _ ->
        val serviceCall = MutableLiveData<NetworkState>()
        serviceCall.value = NetworkState.LOADING
        Handler().postDelayed({
          serviceCall.value = NetworkState.LOADED
        }, 3000)
        serviceCall
      }

  fun login(username: String, password: String) {
    _login.value = Pair(username, password)
  }
}
