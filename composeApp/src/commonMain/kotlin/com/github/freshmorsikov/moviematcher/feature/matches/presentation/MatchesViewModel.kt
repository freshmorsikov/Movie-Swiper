package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MatchesViewModel(
    getPairFlowUseCase: GetPairFlowUseCase
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State.Loading
    }
) {

    init {
        getPairFlowUseCase().onEach { pairState ->
            onAction(
                MatchesUdf.Action.UpdatePair(pairState = pairState)
            )
        }.launchIn(viewModelScope)
    }

    override fun reduce(action: MatchesUdf.Action): MatchesUdf.State {
        return when (action) {
            MatchesUdf.Action.CreatePairClick -> {
                currentState
            }

            MatchesUdf.Action.JoinPairClick -> {
                currentState
            }

            is MatchesUdf.Action.UpdatePair -> {
                when (action.pairState) {
                    is PairState.NotPaired -> {
                        MatchesUdf.State.NotPaired
                    }

                    is PairState.Paired -> {
                        if (action.pairState.matchedMovieList.isEmpty()) {
                            MatchesUdf.State.Empty(code = action.pairState.code)
                        } else {
                            MatchesUdf.State.Data(pairState = action.pairState)
                        }
                    }
                }
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