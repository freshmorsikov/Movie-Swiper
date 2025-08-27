package com.github.freshmorsikov.moviematcher.feature.join.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel

class JoinPairViewModel : UdfViewModel<JoinPairUdf.State, JoinPairUdf.Action, JoinPairUdf.Event>(
    initState = { JoinPairUdf.State }
) {
    override fun reduce(action: JoinPairUdf.Action): JoinPairUdf.State {
        return JoinPairUdf.State
    }

    override suspend fun handleEffects(action: JoinPairUdf.Action) {
        when (action) {
            JoinPairUdf.Action.CloseClick -> {
                sendEvent(JoinPairUdf.Event.GoBack)
            }

            is JoinPairUdf.Action.SaveCode -> {
                // TODO save
            }
        }
    }


}