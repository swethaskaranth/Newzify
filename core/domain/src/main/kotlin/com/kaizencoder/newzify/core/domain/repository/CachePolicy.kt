package com.kaizencoder.newzify.core.domain.repository

interface CachePolicy {

    fun hasCacheExpired(cachedAt: Long): Boolean
}