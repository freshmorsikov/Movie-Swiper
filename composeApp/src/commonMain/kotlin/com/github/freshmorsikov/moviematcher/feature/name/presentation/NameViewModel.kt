package com.github.freshmorsikov.moviematcher.feature.name.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel

class NameViewModel : UdfViewModel<NameUdf.State, NameUdf.Action, NameUdf.Event>(
    initState = {
        NameUdf.State(name = "")
    }
) {

    override fun reduce(action: NameUdf.Action): NameUdf.State {
        return when (action) {
            is NameUdf.Action.UpdateName -> {
                currentState.copy(name = action.value)
            }
        }
    }

    override suspend fun handleEffects(action: NameUdf.Action) {}
}
