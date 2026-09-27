package com.github.freshmorsikov.moviematcher.feature.movielist.di

import com.github.freshmorsikov.moviematcher.feature.movielist.presentation.MovieListViewModel
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val movieListFeatureModule = module {
    viewModel { (watchlistType: WatchlistType) ->
        MovieListViewModel(
            watchlistType = watchlistType,
            getMovieListFlowUseCase = get(),
        )
    }
}
