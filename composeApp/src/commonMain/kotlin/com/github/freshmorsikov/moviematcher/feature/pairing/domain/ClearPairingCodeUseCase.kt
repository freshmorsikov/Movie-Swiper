package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.feature.pairing.data.PairingRepository

class ClearPairingCodeUseCase(
    private val pairingRepository: PairingRepository,
) {

    operator fun invoke() {
        pairingRepository.savePairingCode(code = null)
    }
}
