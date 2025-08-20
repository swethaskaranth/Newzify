package com.kaizencoder.newzify.core.data.repository

import com.kaizencoder.newzify.core.common.Constants
import com.kaizencoder.newzify.core.domain.repository.CachePolicy

class DefaultCachePolicy: CachePolicy {

    override fun hasCacheExpired(cachedAt: Long): Boolean {
        return cachedAt < System.currentTimeMillis() - Constants.TIME_TO_LIVE
    }
}