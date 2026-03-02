package com.github.freshmorsikov.moviematcher.feature.swipe.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository

class UpdateShowPairStatusUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(showPairStatus: Boolean) {
        userRepository.saveShowPairStatus(showPairStatus = showPairStatus)
    }

}