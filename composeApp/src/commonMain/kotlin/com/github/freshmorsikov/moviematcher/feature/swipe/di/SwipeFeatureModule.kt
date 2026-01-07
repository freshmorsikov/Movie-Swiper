package com.github.freshmorsikov.moviematcher.feature.swipe.di

import com.github.freshmorsikov.moviematcher.feature.swipe.domain.CheckUserUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetPairedFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetRoomUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val swipeFeatureModule = module {
    viewModel {
        SwipeViewModel(
            checkUserUseCase = get(),
            loadGenreListUseCase = get(),
            getMovieListUseCase = get(),
            updateMovieStatusUseCase = get(),
            getPairedFlowUseCase = get(),
            getRoomFlowCaseCase = get(),
            analyticsManager = get(),
        )
    }
    factory {
        CheckUserUseCase(userRepository = get())
    }
    factory {
        LoadGenreListUseCase(movieRepository = get())
    }
    factory {
        GetMovieListUseCase(movieRepository = get())
    }
    factory {
        GetRoomUseCase(userRepository = get())
    }
    factory {
        UpdateMovieStatusUseCase(
            movieRepository = get(),
            userRepository = get(),
            reactionRepository = get(),
            matchRepository = get(),
        )
    }
    factory {
        GetPairedFlowUseCase(
            userRepository = get(),
        )
    }
    single {
        MovieRepository(
            movieEntityQueries = get(),
            genreEntityQueries = get(),
            movieWithGenreViewQueries = get(),
            movieGenreReferenceQueries = get(),
            keyValueStore = get(),
            apiService = get(),
            analyticsManager = get(),
        )
    }
}