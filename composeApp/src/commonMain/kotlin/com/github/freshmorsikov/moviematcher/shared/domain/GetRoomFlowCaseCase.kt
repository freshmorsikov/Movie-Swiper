package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.feature.room.data.RoomRepository
import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class GetRoomFlowCaseCase(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<Room> {
        return userRepository.getUserFlow()
            .flatMapLatest { user ->
                roomRepository.getRoomFlowById(roomId = user.room)
            }
    }

}
