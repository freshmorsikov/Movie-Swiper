package com.github.freshmorsikov.moviematcher.feature.no_connection.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.no_connection.domain.CheckConnectivityUseCase

class NoConnectionViewModel(
    private val checkConnectivityUseCase: CheckConnectivityUseCase
) : UdfViewModel<NoConnectionUdf.State, NoConnectionUdf.Action, NoConnectionUdf.Event>(
    initState = { NoConnectionUdf.State }
) {

    override fun reduce(action: NoConnectionUdf.Action): NoConnectionUdf.State {
        return when (action) {
            NoConnectionUdf.Action.Retry -> currentState
        }
    }

    override suspend fun handleEffects(action: NoConnectionUdf.Action) {
        when (action) {
            NoConnectionUdf.Action.Retry -> {
                val isConnected = checkConnectivityUseCase()
                if (isConnected) {
                    sendEvent(NoConnectionUdf.Event.NavigateToEntry)
                }
            }
        }
    }
}
