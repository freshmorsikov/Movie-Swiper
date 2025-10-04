package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsManager
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.join.domain.SaveCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.join.domain.SetPairedUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.analytics.OpenSwipeScreenEvent
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetPairedCodeFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val MOVIE_COUNT = 3

class SwipeViewModel(
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
    private val saveCodeUseCase: SaveCodeUseCase,
    private val setPairedUseCase: SetPairedUseCase,
    private val getPairedCodeFlowUseCase: GetPairedCodeFlowUseCase,
    analyticsManager: AnalyticsManager,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = {
        SwipeUdf.State(
            pairState = null,
            movies = null,
        )
    }
) {

    init {
        analyticsManager.sendEvent(event = OpenSwipeScreenEvent)

        subscribeOnPair()
        viewModelScope.launch {
            loadGenreListUseCase()
        }
        subscribeOnMovieList()
    }

    private fun subscribeOnPair() {
        getPairedCodeFlowUseCase().onEach { code ->
            val pairState = if (code == null) {
                SwipeUdf.PairState.NotLinked
            } else {
                SwipeUdf.PairState.Linked(code = code)
            }
            onAction(SwipeUdf.Action.SetPairState(pairState = pairState))
        }.launchIn(viewModelScope)
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
                currentState.copy(movies = action.movies)
            }

            is SwipeUdf.Action.HandleCode -> {
                if (action.code == null) {
                    currentState
                } else {
                    currentState.copy(pairState = SwipeUdf.PairState.Linking)
                }
            }

            is SwipeUdf.Action.SetPairState -> {
                currentState.copy(
                    pairState = action.pairState
                )
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: SwipeUdf.Action) {
        when (action) {
            is SwipeUdf.Action.FinishSwiping -> {
                val movieStatus = when (action.movieCardState) {
                    SwipeUdf.MovieCardState.Swiped.Left -> MovieStatus.Disliked
                    SwipeUdf.MovieCardState.Swiped.Right -> MovieStatus.Liked
                }
                updateMovieStatus(movieStatus = movieStatus)
            }

            is SwipeUdf.Action.HandleCode -> {
                val code = action.code ?: return

                viewModelScope.launch {
                    val saveCodeJob = launch {
                        saveCodeUseCase(code = code)
                    }
                    val setPairedJob = launch {
                        setPairedUseCase(code = code)
                    }
                    saveCodeJob.join()
                    setPairedJob.join()

                    onAction(
                        SwipeUdf.Action.SetPairState(
                            pairState = SwipeUdf.PairState.Linked(
                                code = code
                            )
                        )
                    )
                }
            }

            else -> {}
        }
    }

    private fun updateMovieStatus(movieStatus: MovieStatus) {
        currentState.movies?.lastOrNull()?.id?.let { id ->
            viewModelScope.launch {
                updateMovieStatusUseCase(
                    id = id,
                    movieStatus = movieStatus,
                )
            }
        }
    }

}