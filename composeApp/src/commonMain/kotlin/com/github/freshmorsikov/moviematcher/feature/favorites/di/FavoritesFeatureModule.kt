package com.github.freshmorsikov.moviematcher.feature.favorites.di

import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesViewModel
import com.github.freshmorsikov.moviematcher.feature.watchlists.domain.WatchlistType
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesFeatureModule = module {
    viewModel { (watchlistType: WatchlistType) ->
        FavoritesViewModel(
            watchlistType = watchlistType,
            getMovieListFlowUseCase = get(),
        )
    }
}
