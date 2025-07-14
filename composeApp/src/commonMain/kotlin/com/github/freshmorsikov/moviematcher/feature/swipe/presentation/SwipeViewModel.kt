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
    private val getMovieListUseCase: GetMovieListUseCase,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = {
        SwipeUdf.State(movieList = emptyList())
    }
) {

    init {
        subscribeOnMovieList()
    }

    private fun subscribeOnMovieList() {
        getMovieListUseCase().onEach { movieList ->
            onAction(
                SwipeUdf.Action.UpdateMovieList(
                    movieList = movieList.reversed()
                )
            )
        }.launchIn(viewModelScope)
    }

    override fun reduce(
        currentState: SwipeUdf.State,
        action: SwipeUdf.Action,
    ): SwipeUdf.State {
        return when (action) {
            is SwipeUdf.Action.UpdateMovieList -> {
                 currentState.copy(movieList = action.movieList)
            }

            SwipeUdf.Action.Like -> {
                updateMovieStatus(status = MovieStatus.Liked)
                currentState
            }

            SwipeUdf.Action.Dislike -> {
                updateMovieStatus(status = MovieStatus.Disliked)
                currentState
            }

            is SwipeUdf.Action.MoreClick -> {
                // TODO implement
                currentState
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