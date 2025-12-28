package com.github.freshmorsikov.moviematcher.core.di

import com.github.freshmorsikov.moviematcher.shared.ui.player.PlayerStateController
import org.koin.dsl.module

actual val platformModule = module {
    single {
        PlayerStateController()
    }
}