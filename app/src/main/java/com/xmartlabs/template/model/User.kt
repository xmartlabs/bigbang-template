package com.xmartlabs.template.model

import com.google.gson.annotations.SerializedName
import com.xmartlabs.bigbang.core.model.EntityWithId

import org.parceler.Parcel

import java.util.ArrayList

data class User(
    override var id: Long = -1,
    @SerializedName("login") var name: String? = null,
    var avatarUrl: String? = null
) : EntityWithId<Long>
