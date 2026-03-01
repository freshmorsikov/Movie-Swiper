package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class GetRoomFlowCaseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Room> {
        return userRepository.getRoomFlow()
            .filterNotNull()
    }

}