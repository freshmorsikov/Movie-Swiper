package com.github.freshmorsikov.moviematcher.feature.swipe.di

import com.github.freshmorsikov.moviematcher.feature.swipe.data.MovieRepository
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val swipeFeatureModule = module {
    viewModel {
        SwipeViewModel(
            loadGenreListUseCase = get(),
            getMovieListUseCase = get(),
            updateMovieStatusUseCase = get(),
        )
    }
    factory {
        LoadGenreListUseCase(movieRepository = get())
    }
    factory {
        GetMovieListUseCase(movieRepository = get())
    }
    factory {
        UpdateMovieStatusUseCase(movieRepository = get())
    }
    single {
        MovieRepository(
            movieEntityQueries = get(),
            genreEntityQueries = get(),
            movieGenreReferenceQueries = get(),
            keyValueStore = get(),
            apiService = get(),
        )
    }
}