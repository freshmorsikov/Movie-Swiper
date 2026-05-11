package com.github.freshmorsikov.moviematcher.feature.room.data

import com.github.freshmorsikov.moviematcher.feature.room.data.mapper.toRoom
import com.github.freshmorsikov.moviematcher.feature.room.data.model.RoomEntity
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class RoomRepository(
    private val roomRemoteDataSource: RoomRemoteDataSource,
) {

    fun getRoomFlowById(roomId: String): Flow<Room> {
        return roomRemoteDataSource.getRoomFlowById(roomId = roomId)
            .filterNotNull()
            .map(RoomEntity::toRoom)
    }

    suspend fun updateRoomGenreFilter(
        roomId: String,
        genreFilter: List<Long>,
    ) {
        roomRemoteDataSource.updateRoomGenreFilter(
            roomId = roomId,
            genreFilter = genreFilter,
        )
    }

}
