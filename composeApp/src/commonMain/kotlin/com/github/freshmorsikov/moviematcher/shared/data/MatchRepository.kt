package com.github.freshmorsikov.moviematcher.shared.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.firstOrNull

class MatchRepository() {

    suspend fun getLiked(code: String): List<Long> {
        return getCollection(
            code = code,
            collection = "liked"
        )
    }

    suspend fun getDisliked(code: String): List<Long> {
        return getCollection(
            code = code,
            collection = "disliked"
        )
    }

    suspend fun getMatched(code: String): List<Long> {
        return getCollection(
            code = code,
            collection = "matched"
        )
    }

    suspend fun addToLiked(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            movieId = movieId,
            collection = "liked"
        )
    }

    suspend fun addToDisliked(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            movieId = movieId,
            collection = "disliked"
        )
    }

    suspend fun addToMatched(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            movieId = movieId,
            collection = "matched"
        )
    }

    suspend fun removeFromLiked(
        code: String,
        movieId: Long,
    ) {
        removeFromCollection(
            code = code,
            movieId = movieId,
            collection = "liked"
        )
    }

    private suspend fun getCollection(
        code: String,
        collection: String,
    ): List<Long> {
        val snapshot = Firebase.database.reference()
            .child("matches")
            .child(code)
            .child(collection)
            .valueEvents
            .firstOrNull()
        return (snapshot?.value as? List<*>)?.mapNotNull { id ->
            id as? Long
        } ?: emptyList()
    }

    private suspend fun addToCollection(
        code: String,
        movieId: Long,
        collection: String,
    ) {
        val reference = Firebase.database.reference()
            .child("matches")
            .child(code)
            .child(collection)
        val snapshot = reference.valueEvents.firstOrNull()
        val listSize = (snapshot?.value as? List<*>)?.size ?: 0
        reference.child(listSize.toString()).setValue(movieId)
    }

    private suspend fun removeFromCollection(
        code: String,
        movieId: Long,
        collection: String,
    ) {
        val reference = Firebase.database.reference()
            .child("matches")
            .child(code)
            .child(collection)
        val snapshot = reference.valueEvents.firstOrNull()
        val list = snapshot?.value as? List<*> ?: return
        val updatedList = list.toMutableList().apply {
            remove(movieId)
        }
        reference.setValue(updatedList)
    }

}