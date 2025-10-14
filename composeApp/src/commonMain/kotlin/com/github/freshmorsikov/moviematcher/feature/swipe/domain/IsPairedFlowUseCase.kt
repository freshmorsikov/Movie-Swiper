package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeFlowCaseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class IsPairedFlowUseCase(
    private val getCodeFlowCaseCase: GetCodeFlowCaseCase,
    private val matchRepository: MatchRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> {
        return getCodeFlowCaseCase().flatMapLatest { code ->
            matchRepository.getPairedFlow(code = code)
        }
    }

}