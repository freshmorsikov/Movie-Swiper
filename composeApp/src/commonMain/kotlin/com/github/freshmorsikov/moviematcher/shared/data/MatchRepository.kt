package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.firstOrNull

private const val PAIR_ID_KEY = "PAIR_ID_KEY"

class MatchRepository(
    private val keyValueStore: KeyValueStore
) {

    suspend fun getPairId(): String? {
        return keyValueStore.getString(PAIR_ID_KEY)
    }

    suspend fun savePairId(pairId: String) {
        keyValueStore.putString(PAIR_ID_KEY, pairId)
    }

    suspend fun getCounter(): Long {
        val snapshot = Firebase.database.reference("counter").valueEvents.firstOrNull()

        return (snapshot?.value as? Long) ?: 0L
    }

    suspend fun updateCounter(counter: Long) {
        Firebase.database.reference("counter").setValue(counter)
    }

    suspend fun getLiked(pairId: String): List<Long> {
        return getCollection(
            pairId = pairId,
            collection = "liked"
        )
    }

    suspend fun getDisliked(pairId: String): List<Long> {
        return getCollection(
            pairId = pairId,
            collection = "disliked"
        )
    }

    suspend fun getMatched(pairId: String): List<Long> {
        return getCollection(
            pairId = pairId,
            collection = "matched"
        )
    }

    suspend fun addToLiked(
        pairId: String,
        movieId: Long,
    ) {
        addToCollection(
            pairId = pairId,
            movieId = movieId,
            collection = "liked"
        )
    }

    suspend fun addToDisliked(
        pairId: String,
        movieId: Long,
    ) {
        addToCollection(
            pairId = pairId,
            movieId = movieId,
            collection = "disliked"
        )
    }

    suspend fun addToMatched(
        pairId: String,
        movieId: Long,
    ) {
        addToCollection(
            pairId = pairId,
            movieId = movieId,
            collection = "matched"
        )
    }

    suspend fun removeFromLiked(
        pairId: String,
        movieId: Long,
    ) {
        removeFromCollection(
            pairId = pairId,
            movieId = movieId,
            collection = "liked"
        )
    }

    private suspend fun getCollection(
        pairId: String,
        collection: String,
    ): List<Long> {
        val snapshot = Firebase.database.reference()
            .child("matches")
            .child(pairId)
            .child(collection)
            .valueEvents
            .firstOrNull()
        return (snapshot?.value as? List<*>)?.mapNotNull { id ->
            id as? Long
        } ?: emptyList()
    }

    private suspend fun addToCollection(
        pairId: String,
        movieId: Long,
        collection: String,
    ) {
        val reference = Firebase.database.reference()
            .child("matches")
            .child(pairId)
            .child(collection)
        val snapshot = reference.valueEvents.firstOrNull()
        val listSize = (snapshot?.value as? List<*>)?.size ?: 0
        reference.child(listSize.toString()).setValue(movieId)
    }

    private suspend fun removeFromCollection(
        pairId: String,
        movieId: Long,
        collection: String,
    ) {
        val reference = Firebase.database.reference()
            .child("matches")
            .child(pairId)
            .child(collection)
        val snapshot = reference.valueEvents.firstOrNull()
        val list = snapshot?.value as? List<*> ?: return
        val updatedList = list.toMutableList().apply {
            remove(movieId)
        }
        reference.setValue(updatedList)
    }

}