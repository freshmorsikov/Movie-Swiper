package com.github.freshmorsikov.moviematcher.app.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairFlowUseCase
import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import kotlinx.coroutines.launch

class AppViewModel(
    private val getPairFlowUseCase: GetPairFlowUseCase,
) : UdfViewModel<AppUdf.State, AppUdf.Action, AppUdf.Event>(
    initState = { AppUdf.State(newMatches = 0) }
) {

    init {
        viewModelScope.launch {
            var currentPairState: PairState? = null
            getPairFlowUseCase().collect { pairState ->
                if (currentPairState == null) {
                    currentPairState = pairState
                } else if (pairState is PairState.Paired) {
                    val currentSize = (currentPairState as? PairState.Paired)?.matchedMovieList?.size ?: 0
                    if (currentSize < pairState.matchedMovieList.size) {
                        onAction(AppUdf.Action.UpdateNewMatches)
                        sendEvent(AppUdf.Event.NewMatchFound)
                    }
                }
            }
        }
    }

    override fun reduce(action: AppUdf.Action): AppUdf.State {
        return when(action) {
            AppUdf.Action.UpdateNewMatches -> {
                currentState.copy(newMatches = currentState.newMatches + 1)
            }
        }
    }

    override suspend fun handleEffects(action: AppUdf.Action) {}

}