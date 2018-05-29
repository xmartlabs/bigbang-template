package com.xmartlabs.template.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.xmartlabs.bigbang.core.model.EntityWithId

import org.parceler.Parcel

import java.util.ArrayList

@Entity
data class User(
    @PrimaryKey override var id: Long = -1,
    @SerializedName("login") var name: String? = null,
    var avatarUrl: String? = null
) : EntityWithId<Long>
