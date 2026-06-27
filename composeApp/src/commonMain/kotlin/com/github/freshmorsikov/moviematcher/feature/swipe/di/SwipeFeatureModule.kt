package com.github.freshmorsikov.moviematcher.feature.swipe.di

import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetPairedFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeViewModel
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val swipeFeatureModule = module {
    viewModel {
        SwipeViewModel(
            getMovieListUseCase = get(),
            updateMovieStatusUseCase = get(),
            getPairedFlowUseCase = get(),
            getRoomFlowCaseCase = get(),
            getInviteLinkUseCase = get(),
            analyticsManager = get(),
        )
    }
    factory {
        GetMovieListUseCase(
            movieRepository = get(),
            getRoomFlowCaseCase = get(),
        )
    }
    factory {
        GetPairedFlowUseCase(
            getRoomFlowCaseCase = get(),
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
            theMovieDbApiService = get(),
            analyticsManager = get(),
        )
    }
}
