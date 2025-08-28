package com.github.freshmorsikov.moviematcher.feature.code.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase
import kotlinx.coroutines.launch

class CodeViewModel(
    private val getCodeUseCase: GetCodeUseCase,
) : UdfViewModel<CodeUdf.State, CodeUdf.Action, CodeUdf.Event>(
    initState = {
        CodeUdf.State(code = "••••")
    }
) {

    init {
        viewModelScope.launch {
            val code = getCodeUseCase()
            onAction(
                CodeUdf.Action.UpdatePairId(code = code)
            )
        }
    }

    override fun reduce(action: CodeUdf.Action): CodeUdf.State {
        return when (action) {
            is CodeUdf.Action.UpdatePairId -> {
                currentState.copy(code = action.code)
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
                        code = currentState.code
                    )
                )
            }

            CodeUdf.Action.ShareClick -> {
                sendEvent(
                    CodeUdf.Event.ShowSharingDialog(
                        code = currentState.code
                    )
                )
            }

            else -> {}
        }
    }
}