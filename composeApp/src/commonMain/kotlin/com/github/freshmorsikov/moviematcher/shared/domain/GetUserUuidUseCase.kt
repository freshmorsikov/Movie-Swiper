package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlin.uuid.ExperimentalUuidApi

// TODO refactor

@OptIn(ExperimentalUuidApi::class)
class GetUserUuidUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): String {
        return userRepository.getUserId() ?: ""
    }

}