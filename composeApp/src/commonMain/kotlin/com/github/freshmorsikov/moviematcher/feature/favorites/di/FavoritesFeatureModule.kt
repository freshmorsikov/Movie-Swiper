package com.github.freshmorsikov.moviematcher.feature.favorites.di

import com.github.freshmorsikov.moviematcher.feature.favorites.domain.GetFavoriteMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.favorites.presentation.FavoritesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesFeatureModule = module {
    viewModel {
        FavoritesViewModel(getFavoriteMovieListUseCase = get())
    }
    factory {
        GetFavoriteMovieListUseCase(movieRepository = get())
    }
}