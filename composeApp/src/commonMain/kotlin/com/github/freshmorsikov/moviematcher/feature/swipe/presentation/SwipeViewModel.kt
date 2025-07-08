package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SwipeViewModel(
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
    getMovieListUseCase: GetMovieListUseCase,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = {
        SwipeUdf.State(
            movieList = emptyList()
        )
    }
) {

    init {
        getMovieListUseCase().onEach { movieList ->
            setState {
                copy(movieList = movieList.reversed())
            }
        }.launchIn(viewModelScope)
    }

    override fun onAction(action: SwipeUdf.Action) {
        when (action) {
            SwipeUdf.Action.Like -> {
                updateMovieStatus(status = MovieStatus.Liked)
            }

            SwipeUdf.Action.Dislike -> {
                updateMovieStatus(status = MovieStatus.Disliked)
            }

            is SwipeUdf.Action.MoreClick -> {
                // TODO implement
            }
        }
    }

    private fun updateMovieStatus(status: MovieStatus) {
        currentState.movieList.lastOrNull()?.let { movie ->
            updateMovieStatusUseCase(
                id = movie.id,
                status = status
            )
        }
    }

}