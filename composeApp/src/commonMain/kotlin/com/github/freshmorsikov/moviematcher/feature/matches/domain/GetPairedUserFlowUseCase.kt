package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.shared.domain.model.User
import kotlinx.coroutines.flow.Flow

class GetPairedUserFlowUseCase(
    private val userRepository: UserRepository,
) {

    operator fun invoke(): Flow<User?> {
        return userRepository.getPairedUserFlow()
    }

}
