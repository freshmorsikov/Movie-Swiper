package com.github.freshmorsikov.moviematcher.feature.matches.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import com.github.freshmorsikov.moviematcher.shared.ui.UserPairState

interface MatchesUdf {

    sealed interface State : Udf.State {
        data object Loading : State
        data class Empty(val userPair: UserPairState) : State
        data class Data(
            val movies: List<Movie>,
            val userPair: UserPairState,
        ) : State

    }

    sealed interface Action : Udf.Action {
        data class UpdateContent(
            val movies: List<Movie>,
            val userPair: UserPairState,
        ) : Action
        data object InviteClick : Action
    }

    sealed interface Event : Udf.Event {
        data class ShowSharingDialog(val inviteLink: String) : Event
    }

}
