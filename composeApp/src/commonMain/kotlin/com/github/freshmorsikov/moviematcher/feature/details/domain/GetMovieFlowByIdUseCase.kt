package com.github.freshmorsikov.moviematcher.feature.details.domain

import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetMovieFlowByIdUseCase() {

    operator fun invoke(id: Long): Flow<Movie> {
        return flowOf()
    }

}