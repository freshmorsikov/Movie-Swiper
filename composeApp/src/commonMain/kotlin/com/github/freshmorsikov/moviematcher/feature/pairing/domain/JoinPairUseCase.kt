package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetUserUuidUseCase

class JoinPairUseCase(
    private val getCodeUseCase: GetCodeUseCase,
    private val userRepository: UserRepository,
    private val getUserUuidUseCase: GetUserUuidUseCase,
) {

    suspend operator fun invoke(code: String): Boolean {
        val currentCode = getCodeUseCase()
        if (currentCode == code) {
            return false
        }

        saveUserCode(code = code)
        return true
    }

    // TODO refactor
    private suspend fun saveUserCode(code: String) {
        val userUuid = getUserUuidUseCase()
        userRepository.saveUserCode(
            userUuid = userUuid,
            code = code
        )
    }

}