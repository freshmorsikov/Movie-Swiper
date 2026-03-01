package com.github.freshmorsikov.moviematcher.feature.name.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.name.domain.SaveUserNameUseCase

class NameViewModel(
    private val saveUserNameUseCase: SaveUserNameUseCase,
) : UdfViewModel<NameUdf.State, NameUdf.Action, NameUdf.Event>(
    initState = {
        NameUdf.State(
            name = "",
            isLoading = false,
        )
    }
) {

    override fun reduce(action: NameUdf.Action): NameUdf.State {
        return when (action) {
            is NameUdf.Action.UpdateName -> {
                currentState.copy(name = action.value)
            }

            is NameUdf.Action.Submit -> {
                currentState.copy(isLoading = true)
            }
        }
    }

    override suspend fun handleEffects(action: NameUdf.Action) {
        when (action) {
            is NameUdf.Action.UpdateName -> Unit
            is NameUdf.Action.Submit -> {
                val name = currentState.name.trim()
                saveUserNameUseCase(name = name)

                sendEvent(NameUdf.Event.NavigateToEntry)
            }
        }
    }

}
