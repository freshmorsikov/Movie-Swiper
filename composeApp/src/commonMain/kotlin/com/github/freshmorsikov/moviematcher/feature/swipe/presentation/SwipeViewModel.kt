package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsManager
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.analytics.OpenSwipeScreenEvent
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.model.MovieStatus
import com.github.freshmorsikov.moviematcher.feature.swipe.presentation.SwipeUdf.SwipeDirection
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val MOVIE_COUNT = 3

class SwipeViewModel(
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
    analyticsManager: AnalyticsManager,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = { SwipeUdf.State.Loading }
) {

    init {
        analyticsManager.sendEvent(event = OpenSwipeScreenEvent)
        viewModelScope.launch {
            loadGenreListUseCase()
        }
        subscribeOnMovieList()
    }

    private fun subscribeOnMovieList() {
        getMovieListUseCase().onEach { movieList ->
            if (movieList.isNotEmpty()) {
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
                SwipeUdf.State.Data(movies = action.movies)
            }

            is SwipeUdf.Action.FinishSwiping -> {
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
            is SwipeUdf.Action.FinishSwiping -> {
                val movieStatus = when (action.direction) {
                    SwipeDirection.Left -> MovieStatus.Disliked
                    SwipeDirection.Right -> MovieStatus.Liked
                }
                updateMovieStatus(movieStatus = movieStatus)
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