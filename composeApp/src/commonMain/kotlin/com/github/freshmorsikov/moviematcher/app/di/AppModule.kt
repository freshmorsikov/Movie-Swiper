package com.github.freshmorsikov.moviematcher.app.di

import com.github.freshmorsikov.moviematcher.app.StartupManager
import com.github.freshmorsikov.moviematcher.app.presentation.AppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        StartupManager()
    }
    viewModel {
        AppViewModel(
            getMatchedListFlowUseCase = get(),
            getUserNameUseCase = get(),
            checkConnectivityUseCase = get(),
        )
    }
}
