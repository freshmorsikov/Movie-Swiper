package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val MATCHES = "matches"
private const val LIKED = "liked"
private const val DISLIKED = "disliked"
private const val MATCHED = "matched"

class MatchRepository(
    private val supabaseApiService: SupabaseApiService
) {

    fun getPairedFlow(roomId: String): Flow<Boolean> {
        return supabaseApiService.getUsersFlowByRoomId(roomId = roomId).map { users ->
            users.size > 1
        }
    }

    fun getMatchedListFlow(code: String): Flow<List<Long>> {
        return Firebase.database.reference()
            .child(MATCHES)
            .child(code)
            .child(MATCHED)
            .valueEvents
            .map { snapshot ->
                snapshot.children.mapNotNull { child ->
                    child.key?.toLongOrNull()
                }
            }
    }

    suspend fun isMovieLiked(code: String, movieId: Long): Boolean {
        return hasMovie(
            code = code,
            collection = LIKED,
            movieId = movieId,
        )
    }

    suspend fun isMovieDisliked(code: String, movieId: Long): Boolean {
        return hasMovie(
            code = code,
            collection = DISLIKED,
            movieId = movieId,
        )
    }

    suspend fun addToLiked(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            movieId = movieId,
            collection = LIKED,
        )
    }

    suspend fun addToDisliked(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            movieId = movieId,
            collection = DISLIKED,
        )
    }

    suspend fun addToMatched(
        code: String,
        movieId: Long,
    ) {
        addToCollection(
            code = code,
            collection = MATCHED,
            movieId = movieId,
        )
    }

    suspend fun removeFromLiked(
        code: String,
        movieId: Long,
    ) {
        removeFromCollection(
            code = code,
            collection = LIKED,
            movieId = movieId,
        )
    }

    suspend fun removeFromDisliked(
        code: String,
        movieId: Long,
    ) {
        removeFromCollection(
            code = code,
            collection = DISLIKED,
            movieId = movieId,
        )
    }

    private suspend fun hasMovie(
        code: String,
        movieId: Long,
        collection: String,
    ): Boolean {
        val snapshot = getMovieReference(
            code = code,
            collection = collection,
            movieId = movieId,
        ).valueEvents.firstOrNull()

        return snapshot?.value != null
    }

    private suspend fun addToCollection(
        code: String,
        collection: String,
        movieId: Long,
    ) {
        val reference = getMovieReference(
            code = code,
            collection = collection,
            movieId = movieId,
        )
        reference.setValue(true)
    }

    private suspend fun removeFromCollection(
        code: String,
        collection: String,
        movieId: Long,
    ) {
        val reference = getMovieReference(
            code = code,
            collection = collection,
            movieId = movieId,
        )
        reference.removeValue()
    }

    private fun getMovieReference(
        code: String,
        collection: String,
        movieId: Long,
    ): DatabaseReference {
        return Firebase.database.reference()
            .child(MATCHES)
            .child(code)
            .child(collection)
            .child(movieId.toString())
    }

}