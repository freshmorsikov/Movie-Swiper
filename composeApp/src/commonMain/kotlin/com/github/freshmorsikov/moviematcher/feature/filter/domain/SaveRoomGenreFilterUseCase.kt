package com.github.freshmorsikov.moviematcher.feature.filter.domain

import com.github.freshmorsikov.moviematcher.feature.user.data.UserRepository

class SaveRoomGenreFilterUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(selectedGenreIds: List<Long>) {
        userRepository.updateRoomGenreFilter(genreFilter = selectedGenreIds)
    }
}
