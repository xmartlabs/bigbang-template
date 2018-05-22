package com.xmartlabs.template.model

import com.google.gson.annotations.SerializedName
import com.xmartlabs.bigbang.core.model.EntityWithId

import org.parceler.Parcel

import java.util.ArrayList

class User : EntityWithId<Long> {
  override var id: Long = -1
  @SerializedName("login")
  internal var name: String? = null
  internal var organizations: List<Organization> = ArrayList()
}
