package com.github.freshmorsikov.moviematcher.feature.join.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.swipe.domain.JoinPairUseCase

class PairingViewModel(
    private val joinPairUseCase: JoinPairUseCase,
) : UdfViewModel<PairingUdf.State, PairingUdf.Action, PairingUdf.Event>(
    initState = { PairingUdf.State.Loading }
) {

    override fun reduce(action: PairingUdf.Action): PairingUdf.State {
        return when (action) {
            is PairingUdf.Action.ShowResult -> {
                PairingUdf.State.Result(isSuccess = action.isSuccess)
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: PairingUdf.Action) {
        when (action) {
            is PairingUdf.Action.HandleCode -> {
                if (action.code == null) {
                    onAction(PairingUdf.Action.ShowResult(isSuccess = false))
                } else {
                    val isSuccess = joinPairUseCase(code = action.code)
                    onAction(PairingUdf.Action.ShowResult(isSuccess = isSuccess))
                }
            }

            else -> {}
        }
    }

}