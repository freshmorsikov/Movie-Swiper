package com.github.freshmorsikov.moviematcher.feature.details.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.details.domain.GetMovieFlowByIdUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.LoadMovieDetailsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieDetailsViewModel(
    movieId: Long,
    getMovieFlowByIdUseCase: GetMovieFlowByIdUseCase,
    loadMovieDetailsUseCase: LoadMovieDetailsUseCase,
) : UdfViewModel<MovieDetailsUdf.State, MovieDetailsUdf.Action, MovieDetailsUdf.Event>(
    initState = {
        MovieDetailsUdf.State.Loading
    }
) {

    init {
        getMovieFlowByIdUseCase(id = movieId)
            .onEach { movie ->
                onAction(MovieDetailsUdf.Action.UpdateMovie(movie = movie))
            }.launchIn(viewModelScope)
        loadMovieDetailsUseCase(id = movieId)
    }

    override fun reduce(action: MovieDetailsUdf.Action): MovieDetailsUdf.State {
        return when (action) {
            is MovieDetailsUdf.Action.UpdateMovie -> {
                MovieDetailsUdf.State.Data(movie = action.movie)
            }
        }
    }

    override suspend fun handleEffects(action: MovieDetailsUdf.Action) {}

}