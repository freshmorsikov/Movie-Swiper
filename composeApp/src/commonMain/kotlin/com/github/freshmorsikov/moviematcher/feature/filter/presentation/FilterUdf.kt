package com.github.freshmorsikov.moviematcher.feature.filter.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.filter.domain.SelectableGenre

interface FilterUdf {

    sealed interface State : Udf.State {
        data object Loading : State

        data class Success(
            val isSaving: Boolean,
            val searchQuery: String,
            val initialGenres: List<SelectableGenre>,
            val currentGenres: List<SelectableGenre>,
            val visibleGenres: List<SelectableGenre>,
            val isApplyEnabled: Boolean,
        ) : State
    }

    sealed interface Action : Udf.Action {
        data class HandleLoadedGenres(
            val genres: List<SelectableGenre>,
        ) : Action

        data class UpdateGenres(
            val currentGenres: List<SelectableGenre>,
            val visibleGenres: List<SelectableGenre>,
        ) : Action

        data class HandleSearchQuery(
            val searchQuery: String,
        ) : Action

        data class UpdateVisibleGenres(
            val searchQuery: String,
            val visibleGenres: List<SelectableGenre>,
        ) : Action

        data class ToggleGenre(
            val genreId: Long,
        ) : Action

        data object BackClick : Action
        data object CancelClick : Action
        data object ApplyClick : Action
    }

    sealed interface Event : Udf.Event {
        data object CloseScreen : Event
    }
}
