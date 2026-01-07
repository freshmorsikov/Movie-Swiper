package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MatchRepository(
    private val supabaseApiService: SupabaseApiService
) {

    fun getMatchedListFlow(roomId: String): Flow<List<Long>> {
        return supabaseApiService.getMatchedListFlowByRoomId(roomId = roomId).map { matchedList ->
            matchedList.map { matched ->
                matched.movie
            }
        }
    }

    suspend fun addToMatched(
        roomId: String,
        movieId: Long,
    ) {
        supabaseApiService.createMatched(
            roomId = roomId,
            movieId = movieId,
        )
    }

}