package com.github.freshmorsikov.moviematcher.feature.name.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository

class GetUserNameUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): String? {
        return userRepository.getUserNameOrNull()
    }
}
