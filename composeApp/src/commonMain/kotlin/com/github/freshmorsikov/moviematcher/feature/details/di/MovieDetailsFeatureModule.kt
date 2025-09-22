package com.github.freshmorsikov.moviematcher.feature.details.di

import com.github.freshmorsikov.moviematcher.feature.details.data.ActorRepository
import com.github.freshmorsikov.moviematcher.feature.details.domain.GetActorsByMovieIdUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.GetMovieFlowByIdUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.LoadMovieDetailsUseCase
import com.github.freshmorsikov.moviematcher.feature.details.presentation.MovieDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val movieDetailsFeatureModule = module {
    viewModel { params ->
        MovieDetailsViewModel(
            movieId = params.get(),
            getMovieFlowByIdUseCase = get(),
            loadMovieDetailsUseCase = get(),
            getActorsByMovieIdUseCase = get(),
        )
    }
    factory {
        GetMovieFlowByIdUseCase(movieRepository = get())
    }
    factory {
        LoadMovieDetailsUseCase(movieRepository = get())
    }
    factory {
        GetActorsByMovieIdUseCase(actorRepository = get())
    }
    single {
        ActorRepository(
            apiService = get(),
            actorEntityQueries = get(),
        )
    }
}