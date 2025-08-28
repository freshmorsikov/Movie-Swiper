package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MatchesViewModel(
    getCodeFlowCaseCase: GetCodeFlowCaseCase,
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State(code = "••••")
    }
) {

    init {
        getCodeFlowCaseCase().onEach { code ->
            onAction(
                MatchesUdf.Action.UpdateCode(code = code)
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
            is MatchesUdf.Action.UpdateCode -> {
                currentState.copy(code = action.code)
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