package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SwipeViewModel(
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = {
        SwipeUdf.State(
            movieList = emptyList(),
            removingMovieId = null,
        )
    }
) {

    init {
        viewModelScope.launch {
            loadGenreListUseCase()
        }
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

    override fun reduce(action: SwipeUdf.Action): SwipeUdf.State {
        return when (action) {
            is SwipeUdf.Action.UpdateMovieList -> {
                currentState.copy(movieList = action.movieList)
            }

            SwipeUdf.Action.Like -> {
                startMovieRemoving()
            }

            SwipeUdf.Action.Dislike -> {
                startMovieRemoving()
            }

            is SwipeUdf.Action.FinishRemoving -> {
                currentState.copy(removingMovieId = null)
            }

            is SwipeUdf.Action.MoreClick -> {
                // TODO implement
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: SwipeUdf.Action) {
        when (action) {
            SwipeUdf.Action.Dislike -> {
                delay(500)
                finishMovieRemoving(movieStatus = MovieStatus.Disliked)
            }

            SwipeUdf.Action.Like -> {
                delay(500)
                finishMovieRemoving(movieStatus = MovieStatus.Liked)
            }

            else -> {}
        }
    }

    private fun startMovieRemoving(): SwipeUdf.State {
        return currentState.copy(
            removingMovieId = currentState.movieList.lastOrNull()?.id
        )
    }

    private fun finishMovieRemoving(movieStatus: MovieStatus) {
        currentState.removingMovieId?.let { id ->
            updateMovieStatusUseCase(
                id = id,
                movieStatus = movieStatus
            )
            onAction(SwipeUdf.Action.FinishRemoving)
        }
    }

}