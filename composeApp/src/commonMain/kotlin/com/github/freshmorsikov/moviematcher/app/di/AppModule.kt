package com.github.freshmorsikov.moviematcher.app.di

import com.github.freshmorsikov.moviematcher.app.presentation.AppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        AppViewModel(getMatchedListFlowUseCase = get())
    }
}