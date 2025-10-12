package com.github.freshmorsikov.moviematcher.feature.join.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PairingViewModel : UdfViewModel<PairingUdf.State, PairingUdf.Action, PairingUdf.Event>(
    initState = { PairingUdf.State.Loading }
) {

    override fun reduce(action: PairingUdf.Action): PairingUdf.State {
        return when (action) {
            is PairingUdf.Action.ShowSuccess -> {
                PairingUdf.State.Success
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: PairingUdf.Action) {
        when (action) {
            is PairingUdf.Action.HandleCode -> {
                viewModelScope.launch {
                    delay(1_000)
                    // TODO handle code
                    onAction(PairingUdf.Action.ShowSuccess)
                }
            }

            else -> {}
        }
    }

}