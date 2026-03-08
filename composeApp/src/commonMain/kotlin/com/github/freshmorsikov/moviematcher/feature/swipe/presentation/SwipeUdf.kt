package com.github.freshmorsikov.moviematcher.feature.swipe.presentation

import com.github.freshmorsikov.moviematcher.core.presentation.Udf
import com.github.freshmorsikov.moviematcher.feature.movie.domain.model.Genre
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie

interface SwipeUdf {

    data class State(
        val code: String?,
        val inviteBannerVisible: Boolean,
        val movies: List<Movie>?,
        val genres: List<Genre>,
    ) : Udf.State

    sealed interface MovieCardState {
        data object Center : MovieCardState
        sealed interface Swiped : MovieCardState {
            data object Left : Swiped
            data object Right : Swiped
        }
    }

    sealed interface Action : Udf.Action {
        data class UpdateMovie(val movies: List<Movie>) : Action
        data class UpdateGenreList(val genres: List<Genre>) : Action
        data class UpdateInviteBanner(val visible: Boolean) : Action
        data class UpdateCode(val code: String) : Action
        data class FinishSwiping(val movieCardState: MovieCardState.Swiped) : Action
        data object InviteClick : Action
    }

    sealed interface Event : Udf.Event {
        data class ShowSharingDialog(val inviteLink: String): Event
    }

}
