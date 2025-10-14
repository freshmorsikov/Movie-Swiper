package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.GetCodeUseCase
import com.github.freshmorsikov.moviematcher.shared.domain.GetUserUuidUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class JoinPairUseCase(
    private val getCodeUseCase: GetCodeUseCase,
    private val userRepository: UserRepository,
    private val getUserUuidUseCase: GetUserUuidUseCase,
    private val matchRepository: MatchRepository,
) {

    suspend operator fun invoke(code: String): Boolean {
        val currentCode = getCodeUseCase()
        if (currentCode == code) {
            return false
        }

        return coroutineScope {
            val savingJob = launch {
                saveUserCode(code = code)
            }
            val pairingJob = launch {
                matchRepository.setPaired(
                    code = code,
                    paired = true
                )
            }
            savingJob.join()
            pairingJob.join()

            true
        }
    }

    private suspend fun saveUserCode(code: String) {
        val userUuid = getUserUuidUseCase()
        userRepository.saveUserCode(
            userUuid = userUuid,
            code = code
        )
    }

}