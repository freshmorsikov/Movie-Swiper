package com.github.freshmorsikov.moviematcher.shared.domain

import com.github.freshmorsikov.moviematcher.shared.data.UserRepository
import com.github.freshmorsikov.moviematcher.util.Constants.LINK_BASE_PATH

class GetInviteLinkUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): String? {
        return userRepository.getRoomCodeOrNull()?.let { code ->
            "$LINK_BASE_PATH?code=$code"
        }
    }

}
