package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase

class JoinPairUseCase(
    private val getCodeUseCase: GetCodeUseCase,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(code: String): Boolean {
        val currentCode = getCodeUseCase()
        if (currentCode == code) {
            return false
        }

        val userId = userRepository.getUserId()
        return userRepository.updateUserCode(
            userId = userId,
            code = code
        )
    }

}