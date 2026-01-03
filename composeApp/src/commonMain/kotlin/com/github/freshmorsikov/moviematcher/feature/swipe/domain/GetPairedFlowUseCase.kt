package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest

class GetPairedFlowUseCase(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> {
        return userRepository.getRoomFlow()
            .filterNotNull()
            .flatMapLatest { room ->
                matchRepository.getPairedFlow(roomId = room.id)
            }
    }

}