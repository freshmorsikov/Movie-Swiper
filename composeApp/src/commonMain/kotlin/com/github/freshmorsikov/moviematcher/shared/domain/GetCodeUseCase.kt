package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class GetCodeUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): String {
        return userRepository.getRoomFlow()
            .filterNotNull()
            .first()
            .code
    }

}