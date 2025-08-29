package com.github.freshmorsikov.moviematcher.feature.join.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.join.domain.SaveCodeUseCase
import com.github.freshmorsikov.moviematcher.feature.join.domain.SetPairedUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.PAIR_ID_ABC
import kotlinx.coroutines.launch

class JoinPairViewModel(
    private val saveCodeUseCase: SaveCodeUseCase,
    private val setPairedUseCase: SetPairedUseCase,
) : UdfViewModel<JoinPairUdf.State, JoinPairUdf.Action, JoinPairUdf.Event>(
    initState = {
        JoinPairUdf.State(code = "")
    }
) {

    override fun reduce(action: JoinPairUdf.Action): JoinPairUdf.State {
        return when (action) {
            is JoinPairUdf.Action.UpdateCode -> {
                val normalizedCode = action.input.filter { char ->
                    PAIR_ID_ABC.contains(
                        char = char,
                        ignoreCase = true,
                    )
                }.map { char ->
                    if (char.isLetter()) {
                        char.uppercase()
                    } else {
                        char
                    }
                }.take(4).joinToString("")

                currentState.copy(code = normalizedCode)
            }

            else -> {
                currentState
            }
        }
    }

    override suspend fun handleEffects(action: JoinPairUdf.Action) {
        when (action) {
            JoinPairUdf.Action.CloseClick -> {
                sendEvent(JoinPairUdf.Event.GoBack)
            }

            is JoinPairUdf.Action.SaveCode -> {
                viewModelScope.launch {
                    val saveCodeJob = launch {
                        saveCodeUseCase(code = currentState.code)
                    }
                    val setPairedJob = launch {
                        setPairedUseCase(code = currentState.code)
                    }

                    saveCodeJob.join()
                    setPairedJob.join()
                    sendEvent(JoinPairUdf.Event.OpenSuccess)
                }
            }

            else -> Unit
        }
    }


}