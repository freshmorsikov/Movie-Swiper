package com.github.freshmorsikov.moviematcher.feature.pairing.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.no_connection.domain.CheckConnectivityUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.GetPairingCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.pairing.domain.SavePairingCodeUseCase

class EntryViewModel(
    private val code: String?,
    private val savePairingCodeUseCase: SavePairingCodeUseCase,
    private val getPairingCodeUseCase: GetPairingCodeUseCase,
    private val checkConnectivityUseCase: CheckConnectivityUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
) : UdfViewModel<EntryUdf.State, EntryUdf.Action, EntryUdf.Event>(
    initState = { EntryUdf.State }
) {

    init {
        onAction(EntryUdf.Action.CheckNext)
    }

    override suspend fun handleEffects(action: EntryUdf.Action) {
        when (action) {
            EntryUdf.Action.CheckNext -> {
                if (code != null) {
                    savePairingCodeUseCase(code = code)
                }
                val navigateEvent = when {
                    checkConnectivityUseCase().not() -> EntryUdf.Event.NavigateToNoConnection
                    getUserNameUseCase().isNullOrBlank() -> EntryUdf.Event.NavigateToName
                    getPairingCodeUseCase() == null -> EntryUdf.Event.NavigateToSwipe
                    else -> EntryUdf.Event.NavigateToPairing
                }
                sendEvent(navigateEvent)
            }
        }
    }

}
