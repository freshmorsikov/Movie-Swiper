package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairIdUseCase
import kotlinx.coroutines.launch

class MatchesViewModel(
    private val getPairIdUseCase: GetPairIdUseCase
) : UdfViewModel<MatchesUdf.State, MatchesUdf.Action, MatchesUdf.Event>(
    initState = {
        MatchesUdf.State(pairId = null)
    }
) {

    init {
        viewModelScope.launch {
            val pairId = getPairIdUseCase()
            onAction(
                MatchesUdf.Action.UpdatePairId(pairId = pairId)
            )
        }
    }

    override fun reduce(action: MatchesUdf.Action): MatchesUdf.State {
        return when (action) {
            is MatchesUdf.Action.UpdatePairId -> {
                currentState.copy(pairId = action.pairId)
            }
        }
    }

    override suspend fun handleEffects(action: MatchesUdf.Action) {}

}