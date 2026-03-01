package com.github.freshmorsikov.moviematcher.app.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetMatchedListFlowUseCase
import kotlinx.coroutines.launch

class AppViewModel(
    private val getMatchedListFlowUseCase: GetMatchedListFlowUseCase,
) : UdfViewModel<AppUdf.State, AppUdf.Action, AppUdf.Event>(
    initState = {
        AppUdf.State(
            newMatches = 0,
            isCurrentMatches = false,
        )
    }
) {

    init {
        viewModelScope.launch {
            var currentSize: Int? = null
            getMatchedListFlowUseCase().collect { movies ->
                currentSize?.let { size ->
                    if (size < movies.size) {
                        onAction(AppUdf.Action.UpdateNewMatches)
                        sendEvent(AppUdf.Event.NewMatchFound)
                    }
                }
                currentSize = movies.size
            }
        }
    }

    override fun reduce(action: AppUdf.Action): AppUdf.State {
        return when (action) {
            AppUdf.Action.UpdateNewMatches -> {
                if (currentState.isCurrentMatches) {
                    currentState.copy(newMatches = 0)
                } else {
                    currentState.copy(newMatches = currentState.newMatches + 1)
                }
            }

            is AppUdf.Action.UpdateIsCurrentMatches -> {
                currentState.copy(
                    newMatches = if (action.isCurrentMatches) {
                        0
                    } else {
                        currentState.newMatches
                    },
                    isCurrentMatches = action.isCurrentMatches,
                )
            }
        }
    }

    override suspend fun handleEffects(action: AppUdf.Action) {}

}
