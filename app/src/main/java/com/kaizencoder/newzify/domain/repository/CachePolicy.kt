package com.kaizencoder.newzify.domain.repository

interface CachePolicy {

    fun hasCacheExpired(cachedAt: Long): Boolean
}