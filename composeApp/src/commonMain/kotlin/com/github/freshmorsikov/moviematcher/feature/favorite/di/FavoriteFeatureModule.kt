package com.github.freshmorsikov.moviematcher.feature.favorite.di

import com.github.freshmorsikov.moviematcher.feature.favorite.domain.GetFavoriteMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.favorite.presentation.FavoriteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoriteFeatureModule = module {
    viewModel {
        FavoriteViewModel(getFavoriteMovieListUseCase = get())
    }
    factory {
        GetFavoriteMovieListUseCase(movieRepository = get())
    }
}