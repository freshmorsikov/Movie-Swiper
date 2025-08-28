package com.github.freshmorsikov.moviematcher.feature.join.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetUserUuidUseCase

class SaveCodeUseCase(
    private val userRepository: UserRepository,
    private val getUserUuidUseCase: GetUserUuidUseCase,
) {

    suspend operator fun invoke(code: String) {
        val userUuid = getUserUuidUseCase()

        userRepository.saveUserCode(
            userUuid = userUuid,
            code = code
        )
    }

}