package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.MatchedEntity
import com.github.freshmorsikov.moviematcher.shared.domain.model.Matched
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MatchRepository(
    private val supabaseApiService: SupabaseApiService
) {

    fun getMatchedListFlow(roomId: String): Flow<List<Matched>> {
        return supabaseApiService.getMatchedListFlowByRoomId(roomId = roomId)
            .map { matchedList ->
                matchedList.map { matched -> matched.toMatched() }
            }
    }

    private fun MatchedEntity.toMatched(): Matched {
        return Matched(
            id = id,
            room = room,
            movie = movie,
            active = active,
        )
    }

}
