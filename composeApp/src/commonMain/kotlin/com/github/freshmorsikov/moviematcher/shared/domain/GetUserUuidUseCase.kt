package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class GetUserUuidUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): String {
        val userUuid = userRepository.getUserUuid()
        return if (userUuid == null) {
            val newUuid = Uuid.random().toString()
            userRepository.saveUserUuid(uuid = newUuid)

            newUuid
        } else {
            userUuid
        }
    }

}