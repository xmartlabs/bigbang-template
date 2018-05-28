package com.xmartlabs.template.repository.common

interface DataSource<T> {
  fun all(): Listing<T>
  fun save(data: T): Boolean
}
