package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.feature.pairing.data.PairingRepository

class GetPairingCodeUseCase(
    private val pairingRepository: PairingRepository,
) {

    operator fun invoke(): String? {
        return pairingRepository.pairingCode
    }

}
