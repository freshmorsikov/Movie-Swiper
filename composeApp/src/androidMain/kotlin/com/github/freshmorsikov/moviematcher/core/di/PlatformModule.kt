package com.github.freshmorsikov.moviematcher.core.di

import com.github.freshmorsikov.moviematcher.shared.ui.player.CachedPlaybackDataSourceFactory
import com.github.freshmorsikov.moviematcher.shared.ui.player.CachedPlaybackDataSourceFactoryImpl
import com.github.freshmorsikov.moviematcher.shared.ui.player.PlayerStateController
import org.koin.dsl.module

actual val platformModule = module {
    single {
        PlayerStateController(
            context = get(),
            cachedPlaybackDataSourceFactory = get(),
        )
    }
    single<CachedPlaybackDataSourceFactory> {
        CachedPlaybackDataSourceFactoryImpl(context = get())
    }
}