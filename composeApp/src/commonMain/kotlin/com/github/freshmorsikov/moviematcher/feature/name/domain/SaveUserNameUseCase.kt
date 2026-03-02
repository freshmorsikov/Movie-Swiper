package com.github.freshmorsikov.moviematcher.feature.name.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.feature.user.domain.CreateUserUseCase

class SaveUserNameUseCase(
    private val createUserUseCase: CreateUserUseCase,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(name: String) {
        val userId = userRepository.getUserIdOrNull()

        if (userId == null) {
            createUserUseCase(name = name)
        } else {
            userRepository.updateUserName(
                userId = userId,
                name = name,
            )
        }
    }

}
