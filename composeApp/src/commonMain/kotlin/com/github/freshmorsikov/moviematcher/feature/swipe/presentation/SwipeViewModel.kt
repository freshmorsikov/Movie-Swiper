package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.analytics.AnalyticsManager
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.analytics.OpenSwipeScreenEvent
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.CheckUserUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetMovieListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.GetPairedFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.LoadGenreListUseCase
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.UpdateMovieStatusUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.MovieStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val MOVIE_COUNT = 3

class SwipeViewModel(
    private val checkUserUseCase: CheckUserUseCase,
    private val loadGenreListUseCase: LoadGenreListUseCase,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val updateMovieStatusUseCase: UpdateMovieStatusUseCase,
    private val getPairedFlowUseCase: GetPairedFlowUseCase,
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
    analyticsManager: AnalyticsManager,
) : UdfViewModel<SwipeUdf.State, SwipeUdf.Action, SwipeUdf.Event>(
    initState = {
        SwipeUdf.State(
            code = null,
            inviteBannerVisible = false,
            movies = null,
        )
    }
) {

    init {
        analyticsManager.sendEvent(event = OpenSwipeScreenEvent)

        subscribeOnPaired()
        subscribeOnCode()
        subscribeOnMovieList()
        viewModelScope.launch {
            checkUserUseCase()
        }
        viewModelScope.launch {
            loadGenreListUseCase()
        }
    }

    private fun subscribeOnPaired() {
        getPairedFlowUseCase().onEach { isPaired ->
            onAction(SwipeUdf.Action.UpdateInviteBanner(visible = !isPaired))
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnCode() {
        getRoomFlowCaseCase().onEach { room ->
            onAction(SwipeUdf.Action.UpdateCode(code = room.code))
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

            is SwipeUdf.Action.UpdateInviteBanner -> {
                currentState.copy(inviteBannerVisible = action.visible)
            }

            is SwipeUdf.Action.UpdateCode -> {
                currentState.copy(
                    code = action.code
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

            is SwipeUdf.Action.InviteClick -> {
                val code = currentState.code ?: return
                val inviteLink = "https://freshmorsikov.github.io/Movie-Swiper-Landing?code=$code"
                sendEvent(SwipeUdf.Event.ShowSharingDialog(inviteLink = inviteLink))
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