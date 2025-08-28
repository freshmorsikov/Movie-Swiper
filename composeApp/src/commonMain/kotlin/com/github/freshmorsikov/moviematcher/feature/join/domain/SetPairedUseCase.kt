package com.github.freshmorsikov.moviematcher.feature.join.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository

class SetPairedUseCase(
    private val matchRepository: MatchRepository
) {

    suspend operator fun invoke(code: String) {
        matchRepository.setPaired(
            code = code,
            paired = true
        )
    }

}