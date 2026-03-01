package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.ClearPairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.GetPairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.JoinPairUseCase

class PairingViewModel(
    getPairingCodeUseCase: GetPairingCodeUseCase,
    private val clearPairingCodeUseCase: ClearPairingCodeUseCase,
    private val joinPairUseCase: JoinPairUseCase,
) : UdfViewModel<PairingUdf.State, PairingUdf.Action, PairingUdf.Event>(
    initState = { PairingUdf.State.Loading }
) {

    init {
        val code = getPairingCodeUseCase()
        onAction(
            PairingUdf.Action.HandleCode(code = code)
        )
    }

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
                    clearPairingCodeUseCase()
                    val isSuccess = joinPairUseCase(code = action.code)
                    onAction(PairingUdf.Action.ShowResult(isSuccess = isSuccess))
                }
            }

            else -> Unit
        }
    }

}