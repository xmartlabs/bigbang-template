package com.xmartlabs.template.model.service

data class ListResponse<out T>(val total_count: Long, val items: List<T>)