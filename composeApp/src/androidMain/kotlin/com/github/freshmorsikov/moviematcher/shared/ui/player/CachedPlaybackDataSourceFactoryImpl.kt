package com.github.freshmorsikov.moviematcher.shared.ui.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import java.io.File

interface CachedPlaybackDataSourceFactory {
    fun buildCacheDataSourceFactory(): DefaultMediaSourceFactory
    fun clearCache()
}


@OptIn(UnstableApi::class)
class CachedPlaybackDataSourceFactoryImpl(
    private val context: Context,
) : CachedPlaybackDataSourceFactory {

    companion object {
        const val CACHE_SIZE = 100L * 1024L * 1024L //100 MB
        const val CACHE_FILE = "player cache"
    }

    private var _cache: SimpleCache? = null

    override fun buildCacheDataSourceFactory(): DefaultMediaSourceFactory {
        val cacheDir = File(context.cacheDir, CACHE_FILE)
        val cache = SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(CACHE_SIZE),
            StandaloneDatabaseProvider(context)
        )
        _cache = cache
        val dataSourceFactory = DefaultDataSource.Factory(
            context,
            CacheDataSource.Factory()
                .setCache(cache)
                .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        )

        return DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory)
    }

    override fun clearCache() {
        _cache?.release()
    }

}