package com.xmartlabs.template.model

import com.google.gson.annotations.SerializedName
import com.xmartlabs.bigbang.core.model.EntityWithId

import org.parceler.Parcel

import java.util.ArrayList

class Repository : EntityWithId<Long> {
  override var id: Long = -1
  internal var owner: User? = null
  internal var name: String? = null
  internal var description: String? = null
  @SerializedName("fork")
  internal var forked: Boolean = false
  internal var language: String? = null
  internal var forksCount: Int = 0
  internal var stargazersCount: Int = 0
  internal var projects: List<Project> = ArrayList()
}
