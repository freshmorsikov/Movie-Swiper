package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.feature.matches.domain.model.PairState
import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetPairFlowUseCase(
    private val getCodeFlowCaseCase: GetCodeFlowCaseCase,
    private val matchRepository: MatchRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<PairState> {
        return getCodeFlowCaseCase().flatMapLatest { code ->
            matchRepository.getPairedFlow(code).map { paired ->
                if (paired) {
                    PairState.Paired(code)
                } else {
                    PairState.NotPaired
                }
            }
        }
    }

}