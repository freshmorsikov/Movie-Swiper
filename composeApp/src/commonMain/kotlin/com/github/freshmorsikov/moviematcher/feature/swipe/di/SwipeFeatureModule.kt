package com.github.freshmorsikov.moviematcher.feature.swipe.di

import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val swipeFeatureModule = module {
    viewModel {
        SwipeViewModel(
            updateMovieStatusUseCase = get(),
            getMovieListUseCase = get(),
        )
    }
    factory {
        UpdateMovieStatusUseCase(movieRepository = get())
    }
    factory {
        GetMovieListUseCase(movieRepository = get())
    }
    single {
        MovieRepository(
            movieEntityQueries = get(),
            keyValueStore = get(),
            apiService = get(),
        )
    }
}