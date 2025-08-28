package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.util.pow
import kotlin.uuid.ExperimentalUuidApi

const val PAIR_ID_ABC = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

@OptIn(ExperimentalUuidApi::class)
class GetCodeUseCase(
    private val getUserUuidUseCase: GetUserUuidUseCase,
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): String {
        val userUuid = getUserUuidUseCase()
        val code = userRepository.getUserCode(userUuid = userUuid)
        return if (code == null) {
            val counter = userRepository.getCodeCounter()
            userRepository.updateCodeCounter(counter = counter + 1)
            val newCode = generateCode(counter = counter)
            userRepository.saveUserCode(
                userUuid = userUuid,
                code = newCode,
            )

            newCode
        } else {
            code
        }
    }

    private fun generateCode(counter: Long): String {
        var pairId = ""
        for (i in 1..4) {
            val letterIndex = counter / PAIR_ID_ABC.length.pow(i - 1) % PAIR_ID_ABC.length.pow(i)
            pairId += PAIR_ID_ABC[letterIndex.toInt()]
        }

        return pairId
    }

}