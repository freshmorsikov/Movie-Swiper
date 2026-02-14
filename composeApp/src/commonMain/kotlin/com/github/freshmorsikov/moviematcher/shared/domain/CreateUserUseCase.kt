package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository

private const val PAIR_ID_ABC = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

class CreateUserUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(name: String) {
        val counter = userRepository.getCodeCounter()
        userRepository.updateCodeCounter(counter = counter + 1)

        val code = generateCode(counter = counter)
        userRepository.createUser(
            code = code,
            name = name,
        )
    }

    private fun generateCode(counter: Long): String {
        var pairId = ""
        var remainder = counter
        repeat(4) {
            val letterIndex = remainder % PAIR_ID_ABC.length
            pairId += PAIR_ID_ABC[letterIndex.toInt()]
            remainder /= PAIR_ID_ABC.length
        }

        return pairId
    }
}
