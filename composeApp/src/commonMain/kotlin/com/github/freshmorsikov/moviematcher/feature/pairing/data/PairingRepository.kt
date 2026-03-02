package com.github.freshmorsikov.moviematcher.feature.pairing.data

class PairingRepository {

    var pairingCode: String? = null
        private set

    fun savePairingCode(code: String?) {
        pairingCode = code
    }
}
