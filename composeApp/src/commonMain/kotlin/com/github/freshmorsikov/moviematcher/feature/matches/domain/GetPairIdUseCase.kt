package com.github.freshmorsikov.moviematcher.feature.matches.domain

import com.github.freshmorsikov.moviematcher.shared.data.MatchRepository
import com.github.freshmorsikov.moviematcher.util.pow

const val PAIR_ID_ABC = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

class GetPairIdUseCase(
    private val matchRepository: MatchRepository
) {

    suspend operator fun invoke(): String {
        val pairId = matchRepository.getPairId()
        return if (pairId == null) {
            val counter = matchRepository.getCounter()
            matchRepository.updateCounter(counter = counter + 1)
            val newPairId = createPairId(counter = counter)
            matchRepository.savePairId(pairId = newPairId)

            newPairId
        } else {
            pairId
        }
    }

    private fun createPairId(counter: Long): String {
        var pairId = ""
        for (i in 1..4) {
            val letterIndex = counter / PAIR_ID_ABC.length.pow(i - 1) % PAIR_ID_ABC.length.pow(i)
            pairId += PAIR_ID_ABC[letterIndex.toInt()]
        }

        return pairId
    }

}