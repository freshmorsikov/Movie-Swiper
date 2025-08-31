package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import com.github.freshmorsikov.moviematcher.shared.data.MovieRepository
import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetPairFlowUseCase(
    private val getCodeFlowCaseCase: GetCodeFlowCaseCase,
    private val matchRepository: MatchRepository,
    private val movieRepository: MovieRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<PairState> {
        return getCodeFlowCaseCase().flatMapLatest { code ->
            matchRepository.getPairedFlow(code = code).flatMapLatest { paired ->
                if (paired) {
                    matchRepository.getMatchedListFlow(code = code).map { matchedList ->
                        val matchedMovieList = movieRepository.getMoviesByIds(ids = matchedList)
                        PairState.Paired(
                            code = code,
                            matchedMovieList = matchedMovieList,
                        )
                    }
                } else {
                    flowOf(PairState.NotPaired(code = code))
                }
            }
        }
    }

}