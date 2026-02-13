package com.github.freshmorsikov.moviematcher.feature.name.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository

class SaveUserNameUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(name: String) {
        userRepository.saveUserName(name = name)
    }
}
