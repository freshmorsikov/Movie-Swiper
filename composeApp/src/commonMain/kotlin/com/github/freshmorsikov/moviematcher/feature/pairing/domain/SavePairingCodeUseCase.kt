package com.github.freshmorsikov.moviematcher.feature.pairing.domain

import com.github.freshmorsikov.moviematcher.feature.pairing.data.PairingRepository

class SavePairingCodeUseCase(
    private val pairingRepository: PairingRepository,
) {

    operator fun invoke(code: String) {
        pairingRepository.savePairingCode(code = code)
    }
}
