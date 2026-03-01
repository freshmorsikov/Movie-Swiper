package com.github.freshmorsikov.moviematcher.feature.details.presentation

import androidx.lifecycle.viewModelScope
import com.github.freshmorsikov.moviematcher.core.presentation.UdfViewModel
import com.github.freshmorsikov.moviematcher.feature.details.domain.GetActorsByMovieIdUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.GetMovieFlowByIdUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.LoadMovieDetailsUseCase
import com.github.freshmorsikov.moviematcher.feature.details.domain.model.Actor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Long,
    getMovieFlowByIdUseCase: GetMovieFlowByIdUseCase,
    loadMovieDetailsUseCase: LoadMovieDetailsUseCase,
    getActorsByMovieIdUseCase: GetActorsByMovieIdUseCase,
) : UdfViewModel<MovieDetailsUdf.State, MovieDetailsUdf.Action, MovieDetailsUdf.Event>(
    initState = {
        MovieDetailsUdf.State.Loading
    }
) {

    private var actors: List<Actor>? = null

    init {
        getMovieFlowByIdUseCase(id = movieId)
            .onEach { movie ->
                onAction(
                    MovieDetailsUdf.Action.UpdateMovie(
                        movie = movie,
                        actors = actors,
                    )
                )
            }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            loadMovieDetailsUseCase(movieId = movieId)
        }
        viewModelScope.launch {
            val receivedActors = getActorsByMovieIdUseCase(movieId = movieId)
            actors = receivedActors
            onAction(MovieDetailsUdf.Action.UpdateActors(actors = receivedActors))
        }
    }

    override fun reduce(action: MovieDetailsUdf.Action): MovieDetailsUdf.State {
        return when (action) {
            is MovieDetailsUdf.Action.UpdateMovie -> {
                MovieDetailsUdf.State.Data(
                    movie = action.movie,
                    actors = action.actors,
                )
            }

            is MovieDetailsUdf.Action.UpdateActors -> {
                (currentState as? MovieDetailsUdf.State.Data)
                    ?.copy(actors = action.actors)
                    ?: currentState
            }
        }
    }

}