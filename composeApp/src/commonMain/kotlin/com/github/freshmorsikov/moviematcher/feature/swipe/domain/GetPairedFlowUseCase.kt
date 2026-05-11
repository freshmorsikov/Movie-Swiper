package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetPairedFlowUseCase(
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
    private val userRepository: UserRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> {
        return getRoomFlowCaseCase().flatMapLatest { room ->
            userRepository.getPairedFlow(roomId = room.id)
        }
    }

}