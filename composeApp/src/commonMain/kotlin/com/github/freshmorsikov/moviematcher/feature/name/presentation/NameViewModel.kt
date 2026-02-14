package com.github.freshmorsikov.moviematcher.feature.name.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.name.domain.GetUserNameUseCase
import com.github.freshmorsikov.moviematcher.feature.name.domain.SaveUserNameUseCase
import kotlinx.coroutines.launch

class NameViewModel(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase,
) : UdfViewModel<NameUdf.State, NameUdf.Action, NameUdf.Event>(
    initState = {
        NameUdf.State(
            name = "",
            isLoading = false,
        )
    }
) {

    init {
        viewModelScope.launch {
            val name = getUserNameUseCase()
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
                currentState.copy(isLoading = true)
            }
        }
    }

    override suspend fun handleEffects(action: NameUdf.Action) {
        when (action) {
            is NameUdf.Action.UpdateName -> Unit
            NameUdf.Action.Submit -> {
                val name = currentState.name.trim()

                saveUserNameUseCase(name = name)
                sendEvent(NameUdf.Event.NavigateToSwipe)
            }
        }
    }
}
