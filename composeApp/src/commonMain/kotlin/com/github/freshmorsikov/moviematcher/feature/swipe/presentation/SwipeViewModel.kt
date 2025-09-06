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

class SwipeViewModel(
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = { SwipeUdf.State.Loading }
) {

    private var zIndex = 1000f

    init {
        viewModelScope.launch {
            loadGenreListUseCase()
        }
        subscribeOnMovieList()
    }

    private fun subscribeOnMovieList() {
        getMovieListUseCase().onEach { movieList ->
            movieList.lastOrNull()?.let { movie ->
                onAction(SwipeUdf.Action.UpdateMovie(movie = movie))
            }
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: SwipeUdf.Action): SwipeUdf.State {
        return when (action) {
            is SwipeUdf.Action.UpdateMovie -> {
                zIndex--
                SwipeUdf.State.Data(
                    movies = listOf(action.movie),
                    swipeDirection = null,
                    zIndex = 1f,
                    isSwiping = false,
                )
            }

            SwipeUdf.Action.Like -> {
                (currentState as? SwipeUdf.State.Data)?.copy(
                    swipeDirection = SwipeDirection.Right,
                    isSwiping = true
                ) ?: currentState
            }

            SwipeUdf.Action.Dislike -> {
                (currentState as? SwipeUdf.State.Data)?.copy(
                    swipeDirection = SwipeDirection.Left,
                    isSwiping = true
                ) ?: currentState
            }

            SwipeUdf.Action.FinishSwiping -> {
                (currentState as? SwipeUdf.State.Data)?.copy(isSwiping = false) ?: currentState
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
                updateMovieStatus(movieStatus = MovieStatus.Disliked)
            }

            SwipeUdf.Action.Like -> {
                updateMovieStatus(movieStatus = MovieStatus.Liked)
            }

            else -> {}
        }
    }

    private fun updateMovieStatus(movieStatus: MovieStatus) {
        val state = (currentState as? SwipeUdf.State.Data) ?: return

        state.movies.firstOrNull()?.id?.let { id ->
            viewModelScope.launch {
                updateMovieStatusUseCase(
                    id = id,
                    movieStatus = movieStatus,
                )
            }
        }
        viewModelScope.launch {
            delay(500)
            onAction(SwipeUdf.Action.FinishSwiping)
        }
    }

}