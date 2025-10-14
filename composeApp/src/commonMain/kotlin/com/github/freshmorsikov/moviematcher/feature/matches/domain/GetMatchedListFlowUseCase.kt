package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import com.github.freshmorsikov.moviematcher.shared.domain.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetMatchedListFlowUseCase(
    private val getCodeFlowCaseCase: GetCodeFlowCaseCase,
    private val matchRepository: MatchRepository,
    private val movieRepository: MovieRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Movie>> {
        return getCodeFlowCaseCase().flatMapLatest { code ->
            matchRepository.getMatchedListFlow(code = code)
                .map { matchedList ->
                    movieRepository.getMoviesByIds(ids = matchedList)
                }
        }
    }

}