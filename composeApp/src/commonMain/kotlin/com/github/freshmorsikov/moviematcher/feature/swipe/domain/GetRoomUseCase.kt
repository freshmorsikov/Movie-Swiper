package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room

class GetRoomUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Room? {
        return userRepository.getRoom()
    }

}