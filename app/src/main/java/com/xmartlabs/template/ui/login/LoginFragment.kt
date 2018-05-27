package com.xmartlabs.template.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xmartlabs.template.service.NetworkState
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.xmartlabs.bigbang.ui.BaseFragment
import com.xmartlabs.template.App
import com.xmartlabs.template.R
import com.xmartlabs.template.ui.Henson
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

@FragmentWithArgs
class LoginFragment : BaseFragment() {
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var userViewModel: LoginViewModel

  override val layoutResId = R.layout.fragment_login

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    userViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(LoginViewModel::class.java)

    userViewModel.login.observe(this, Observer { state ->
      when (state) {
        NetworkState.LOADING -> setIsLoading(true)
        NetworkState.LOADED -> gotoRecyclerExampleActivity()
      }
    })

    loginButton.setOnClickListener { _ ->
      //      if (!username.text.isEmpty() && !password.text.isEmpty() ) {
      userViewModel.login(username.text.toString(), password.text.toString())
//      }
    }
  }

  fun setIsLoading(loading: Boolean) {
    loginButton.visibility = if (loading) View.GONE else View.VISIBLE
    progressBar.visibility = if (loading) View.VISIBLE else View.GONE
  }

  fun gotoRecyclerExampleActivity() {
    val intent = Henson.with(App.context)
        .gotoListUsersActivity()
        .build()
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(intent)
  }
}
