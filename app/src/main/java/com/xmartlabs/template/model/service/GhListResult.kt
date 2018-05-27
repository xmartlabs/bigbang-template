package com.xmartlabs.template.model.service

data class GhListResult<out T>(val total_count: Long, val items: List<T>)