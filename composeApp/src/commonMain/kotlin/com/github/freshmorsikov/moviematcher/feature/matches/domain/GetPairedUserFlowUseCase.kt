package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository
import com.github.freshmorsikov.moviematcher.feature.user.domain.User
import kotlinx.coroutines.flow.Flow

class GetPairedUserFlowUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getPairedUserFlow()
    }

}
