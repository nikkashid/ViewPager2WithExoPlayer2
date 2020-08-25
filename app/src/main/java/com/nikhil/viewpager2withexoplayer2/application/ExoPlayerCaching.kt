package com.nikhil.viewpager2withexoplayer2.application

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class ExoPlayerCaching {

    companion object {
        var simpleCache: SimpleCache? = null
        var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
        var exoDatabaseProvider: ExoDatabaseProvider? = null
    }

    fun init(context: Context, myExoDir: File) {
        if (leastRecentlyUsedCacheEvictor == null) {
            val exoPlayerCacheSize: Long = StorageUtil.getCacheMemoryForInternalMemory()

            leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize)
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = ExoDatabaseProvider(context)
        }

        if (simpleCache == null) {
            simpleCache = SimpleCache(myExoDir, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
        }
    }
}