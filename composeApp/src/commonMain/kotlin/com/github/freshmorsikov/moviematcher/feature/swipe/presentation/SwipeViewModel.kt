package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf.SwipeDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val MOVIE_COUNT = 3

class SwipeViewModel(
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = { SwipeUdf.State.Loading }
) {

    private var currentMovieStatus: MovieStatus? = null

    init {
        viewModelScope.launch {
            loadGenreListUseCase()
        }
        subscribeOnMovieList()
    }

    private fun subscribeOnMovieList() {
        getMovieListUseCase().onEach { movieList ->
            if (movieList.isNotEmpty()) {
                delay(1_000)
                onAction(
                    SwipeUdf.Action.UpdateMovie(
                        movies = movieList.takeLast(MOVIE_COUNT + 1)
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: SwipeUdf.Action): SwipeUdf.State {
        return when (action) {
            is SwipeUdf.Action.UpdateMovie -> {
                SwipeUdf.State.Data(
                    movies = action.movies,
                    swipe = null,
                )
            }

            SwipeUdf.Action.Like -> {
                val state = (currentState as? SwipeUdf.State.Data) ?: return currentState
                if (state.swipe != null) {
                    return currentState
                }

                state.copy(swipe = SwipeDirection.Right)
            }

            SwipeUdf.Action.Dislike -> {
                val state = (currentState as? SwipeUdf.State.Data) ?: return currentState
                if (state.swipe != null) {
                    return currentState
                }

                state.copy(swipe = SwipeDirection.Left)
            }

            SwipeUdf.Action.FinishSwiping -> {
                currentState
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
                currentMovieStatus = MovieStatus.Disliked
            }

            SwipeUdf.Action.Like -> {
                currentMovieStatus = MovieStatus.Liked
            }

            SwipeUdf.Action.FinishSwiping -> {
                currentMovieStatus?.let { movieStatus ->
                    updateMovieStatus(movieStatus = movieStatus)
                }
                currentMovieStatus = null
            }

            else -> {}
        }
    }

    private fun updateMovieStatus(movieStatus: MovieStatus) {
        val state = (currentState as? SwipeUdf.State.Data) ?: return

        state.movies.lastOrNull()?.id?.let { id ->
            viewModelScope.launch {
                updateMovieStatusUseCase(
                    id = id,
                    movieStatus = movieStatus,
                )
            }
        }
    }

}