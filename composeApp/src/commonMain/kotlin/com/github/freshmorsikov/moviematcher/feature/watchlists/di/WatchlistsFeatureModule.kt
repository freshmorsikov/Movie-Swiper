package com.github.freshmorsikov.moviematcher.feature.watchlists.di

import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.GetMovieWatchlistsFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.watchlists.presentation.WatchlistsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val watchlistsFeatureModule = module {
    viewModel {
        WatchlistsViewModel(
            getMovieWatchlistsFlowUseCase = get(),
        )
    }
    factory {
        GetMovieWatchlistsFlowUseCase(
            movieRepository = get(),
            getMatchedListFlowUseCase = get(),
        )
    }
}
