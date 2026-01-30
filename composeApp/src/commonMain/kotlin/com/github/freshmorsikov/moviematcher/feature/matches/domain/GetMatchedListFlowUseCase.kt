package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetMatchedListFlowUseCase(
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
    private val matchRepository: MatchRepository,
    private val movieRepository: MovieRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Movie>> {
        return getRoomFlowCaseCase().flatMapLatest { room ->
            matchRepository.getMatchedListFlow(roomId = room.id)
                .map { matchedList ->
                    movieRepository.getMoviesByIds(ids = matchedList)
                }
        }
    }

}