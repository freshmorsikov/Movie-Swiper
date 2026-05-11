package com.github.freshmorsikov.moviematcher.feature.filter.domain

import com.github.freshmorsikov.moviematcher.feature.room.data.RoomRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetRoomFlowCaseCase
import kotlinx.coroutines.flow.first

class SaveRoomGenreFilterUseCase(
    private val getRoomFlowCaseCase: GetRoomFlowCaseCase,
    private val roomRepository: RoomRepository,
) {

    suspend operator fun invoke(selectedGenreIds: List<Long>) {
        val room = getRoomFlowCaseCase().first()
        roomRepository.updateRoomGenreFilter(
            roomId = room.id,
            genreFilter = selectedGenreIds,
        )
    }
}
