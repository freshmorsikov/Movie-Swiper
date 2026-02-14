package com.github.freshmorsikov.moviematcher.feature.name.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface NameUdf {

    data class State(
        val name: String,
        val isLoading: Boolean,
    ) : Udf.State

    sealed interface Action : Udf.Action {
        data class UpdateName(val value: String) : Action
        data object Submit : Action
    }

    sealed interface Event : Udf.Event {
        data object NavigateToSwipe : Event
    }
}
