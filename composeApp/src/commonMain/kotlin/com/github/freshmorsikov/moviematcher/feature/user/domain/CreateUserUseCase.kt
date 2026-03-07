package com.github.freshmorsikov.moviematcher.feature.user.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository

private const val CODE_ABC = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

class CreateUserUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(name: String) {
        val counter = userRepository.incrementCodeCounter()
        val code = generateCode(counter = counter)
        userRepository.createUser(
            code = code,
            name = name,
        )
    }

    private fun generateCode(counter: Long): String {
        var code = ""
        var remainder = counter
        repeat(4) {
            val letterIndex = remainder % CODE_ABC.length
            code += CODE_ABC[letterIndex.toInt()]
            remainder /= CODE_ABC.length
        }

        return code
    }
}
