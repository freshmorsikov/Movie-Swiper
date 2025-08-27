package com.github.freshmorsikov.moviematcher.feature.code.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.matches.domain.GetPairIdUseCase
import kotlinx.coroutines.launch

class CodeViewModel(
    private val getPairIdUseCase: GetPairIdUseCase,
) : UdfViewModel<CodeUdf.State, CodeUdf.Action, CodeUdf.Event>(
    initState = {
        CodeUdf.State(pairId = "••••")
    }
) {

    init {
        viewModelScope.launch {
            val pairId = getPairIdUseCase()
            onAction(
                CodeUdf.Action.UpdatePairId(pairId = pairId)
            )
        }
    }

    override fun reduce(action: CodeUdf.Action): CodeUdf.State {
        return when (action) {
            is CodeUdf.Action.UpdatePairId -> {
                currentState.copy(pairId = action.pairId)
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: CodeUdf.Action) {
        when (action) {
            CodeUdf.Action.CloseClick -> {
                sendEvent(CodeUdf.Event.GoBack)
            }

            CodeUdf.Action.CopyClick -> {
                sendEvent(
                    CodeUdf.Event.SaveToClipboard(
                        pairId = currentState.pairId
                    )
                )
            }

            CodeUdf.Action.ShareClick -> {
                sendEvent(
                    CodeUdf.Event.ShowSharingDialog(
                        pairId = currentState.pairId
                    )
                )
            }

            else -> {}
        }
    }
}