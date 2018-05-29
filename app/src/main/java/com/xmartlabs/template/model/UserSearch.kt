package com.xmartlabs.template.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

/**
 * Created by mirland on 29/05/18.
 */
@Entity(foreignKeys = [
  ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
])
data class UserSearch(
    val search: String,
    val userId: Long,
    val searchPosition: Int
)
