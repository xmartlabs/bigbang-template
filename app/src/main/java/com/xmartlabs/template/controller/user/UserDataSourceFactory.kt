package com.xmartlabs.template.controller.user

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.xmartlabs.template.model.User
import com.xmartlabs.template.service.UserService

class UserDataSourceFactory(private val userService: UserService, private val userName: String)
    : DataSource.Factory<Int, User>() {
    val sourceLiveData = MutableLiveData<PageKeyedSubredditDataSource>()
    override fun create(): DataSource<Int, User> {
        val source = PageKeyedSubredditDataSource(userService, userName)
        sourceLiveData.postValue(source)
        return source
    }
}
