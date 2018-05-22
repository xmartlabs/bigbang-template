package com.xmartlabs.template.model

import com.xmartlabs.bigbang.core.model.EntityWithId

import org.parceler.Parcel

class Project : EntityWithId<Long> {
  override var id: Long = -1
  internal var name: String? = null
  internal var creator: User? = null
  internal var repository: String? = null
  internal var body: String? = null
  internal var columns: List<Column>? = null
}
