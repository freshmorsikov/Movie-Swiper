package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetCodeFlowCaseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<String> {
        return userRepository.getRoomFlow()
            .filterNotNull()
            .map { room ->
                room.code
            }
    }

}