package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.shared.data.UserRepository

class PairingEntryViewModel(
    private val code: String?,
    private val userRepository: UserRepository,
) : UdfViewModel<PairingEntryUdf.State, PairingEntryUdf.Action, PairingEntryUdf.Event>(
    initState = {
        PairingEntryUdf.State(
            isLoading = true,
        )
    }
) {

    init {
        onAction(PairingEntryUdf.Action.CheckUser)
    }

    override fun reduce(action: PairingEntryUdf.Action): PairingEntryUdf.State {
        return when (action) {
            PairingEntryUdf.Action.CheckUser -> currentState
        }
    }

    override suspend fun handleEffects(action: PairingEntryUdf.Action) {
        when (action) {
            PairingEntryUdf.Action.CheckUser -> {
                val hasUserName = userRepository.getUserNameOrNull().isNullOrBlank().not()
                if (hasUserName) {
                    sendEvent(PairingEntryUdf.Event.NavigateToPairing(code = code))
                } else {
                    sendEvent(PairingEntryUdf.Event.NavigateToName(code = code))
                }
            }
        }
    }
}
