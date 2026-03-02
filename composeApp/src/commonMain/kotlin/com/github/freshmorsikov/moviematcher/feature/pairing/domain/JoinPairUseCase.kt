package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import kotlinx.coroutines.flow.first

class JoinPairUseCase(
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(code: String): Boolean {
        val currentRoom = getRoomFlowCaseCase().first()
        if (currentRoom.code == code) {
            return false
        }

        val userId = userRepository.getUserId()
        return userRepository.updateRoom(
            userId = userId,
            code = code
        )
    }

}