package com.github.freshmorsikov.moviematcher.feature.name.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf

interface NameUdf {

    data class State(
        val name: String,
    ) : Udf.State

    sealed interface Action : Udf.Action {
        data class UpdateName(val value: String) : Action
    }

    sealed interface Event : Udf.Event
}
