package com.github.freshmorsikov.moviematcher.feature.name.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlinx.coroutines.launch

class NameViewModel(
    private val userRepository: UserRepository,
) : UdfViewModel<NameUdf.State, NameUdf.Action, NameUdf.Event>(
    initState = {
        NameUdf.State(name = "")
    }
) {

    init {
        viewModelScope.launch {
            val name = userRepository.getUserNameOrNull()
            if (!name.isNullOrBlank()) {
                sendEvent(NameUdf.Event.NavigateToSwipe)
            }
        }
    }

    override fun reduce(action: NameUdf.Action): NameUdf.State {
        return when (action) {
            is NameUdf.Action.UpdateName -> {
                currentState.copy(name = action.value)
            }

            NameUdf.Action.Submit -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: NameUdf.Action) {
        when (action) {
            is NameUdf.Action.UpdateName -> Unit
            NameUdf.Action.Submit -> {
                val name = currentState.name.trim()
                if (name.isBlank()) return

                userRepository.saveUserName(name = name)
                sendEvent(NameUdf.Event.NavigateToSwipe)
            }
        }
    }
}
