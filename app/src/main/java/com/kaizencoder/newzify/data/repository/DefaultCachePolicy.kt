package com.kaizencoder.newzify.data.repository

import com.kaizencoder.newzify.Constants
import com.kaizencoder.newzify.domain.repository.CachePolicy

class DefaultCachePolicy: CachePolicy {

    override fun hasCacheExpired(cachedAt: Long): Boolean {
        return cachedAt < System.currentTimeMillis() - Constants.TIME_TO_LIVE
    }
}