package com.github.freshmorsikov.moviematcher.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class UdfViewModel<S : Udf.State, A : Udf.Action, E : Udf.Event>(
    initState: () -> S
) : ViewModel() {

    protected val mState = MutableStateFlow(value = initState())
    val state = mState.asStateFlow()
    protected val currentState: S
        get() = mState.value

    protected val mEvent = Channel<E>()
    val event: Flow<E> = mEvent.receiveAsFlow()

    fun onAction(action: A) {
        mState.update {
            reduce(action = action)
        }
        viewModelScope.launch {
            handleEffects(action = action)
        }
    }

    protected abstract fun reduce(action: A): S

    protected abstract suspend fun handleEffects(action: A)

    protected fun sendEvent(event: E) {
        viewModelScope.launch {
            mEvent.send(event)
        }
    }

}