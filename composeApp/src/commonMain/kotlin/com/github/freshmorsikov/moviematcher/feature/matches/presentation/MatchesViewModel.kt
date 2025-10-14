package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MatchesViewModel(
    getMatchedListFlowUseCase: GetMatchedListFlowUseCase
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State.Loading
    }
) {

    init {
        getMatchedListFlowUseCase().onEach { movies ->
            onAction(
                MatchesUdf.Action.UpdateMovies(movies = movies)
            )
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: MatchesUdf.Action): MatchesUdf.State {
        return when (action) {
            is MatchesUdf.Action.UpdateMovies -> {
                if (action.movies.isEmpty()) {
                    MatchesUdf.State.Empty
                } else {
                    MatchesUdf.State.Data(movies = action.movies)
                }
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: MatchesUdf.Action) {
        when (action) {
            MatchesUdf.Action.CreatePairClick -> {
                sendEvent(MatchesUdf.Event.OpenCode)
            }

            MatchesUdf.Action.JoinPairClick -> {
                sendEvent(MatchesUdf.Event.OpenJoinPair)
            }

            else -> {}
        }
    }

}