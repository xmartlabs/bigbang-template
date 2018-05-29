package com.xmartlabs.template.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * Created by mirland on 29/05/18.
 */
@Entity(foreignKeys = [
  ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"])
])
data class UserSearch(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val search: String,
    val userId: Long,
    val searchPosition: Int
)
