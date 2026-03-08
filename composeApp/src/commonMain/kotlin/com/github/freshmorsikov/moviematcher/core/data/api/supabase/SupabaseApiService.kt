package com.github.freshmorsikov.moviematcher.core.data.api.supabase

import com.github.freshmorsikov.moviematcher.core.data.api.safeCall
import com.github.freshmorsikov.moviematcher.core.data.api.safeFlow
import com.github.freshmorsikov.moviematcher.feature.user.data.model.IncrementCounterResponse
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertMatched
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertReaction
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertRoom
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.MatchedEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.ReactionEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.RoomEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

private const val ROOM_TABLE = "room"
private const val MATCHED_TABLE = "matched"
private const val REACTION_TABLE = "reaction"
private const val INCREMENT_COUNTER_FUNCTION = "increment-counter"

private const val ROOM_COLUMN = "room"

class SupabaseApiService(
    private val supabaseClient: SupabaseClient
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun incrementCounter(): Long? {
        return safeCall {
            val response = supabaseClient.functions.invoke(function = INCREMENT_COUNTER_FUNCTION)
            json.decodeFromString<IncrementCounterResponse>(response.bodyAsText()).value
        }
    }

    // ROOM

    suspend fun getRoomById(roomId: String): RoomEntity? {
        return safeCall {
            supabaseClient.from(table = ROOM_TABLE)
                .select {
                    filter { RoomEntity::id eq roomId }
                }.decodeSingleOrNull<RoomEntity>()
        }
    }

    suspend fun getRoomByCode(code: String): RoomEntity? {
        return safeCall {
            supabaseClient.from(table = ROOM_TABLE)
                .select {
                    filter { RoomEntity::code eq code }
                }.decodeSingleOrNull<RoomEntity>()
        }
    }

    suspend fun createRoom(code: String): RoomEntity? {
        return safeCall {
            supabaseClient.from(table = ROOM_TABLE)
                .insert(
                    value = InsertRoom(
                        code = code,
                        genreFilter = emptyList(),
                    )
                ) {
                    select()
                }.decodeSingle<RoomEntity>()
        }
    }

    // REACTION

    suspend fun getReaction(
        userId: String,
        movieId: Long,
        action: ReactionEntity.Action,
    ): ReactionEntity? {
        return safeCall {
            supabaseClient.from(table = REACTION_TABLE)
                .select {
                    filter {
                        and {
                            ReactionEntity::user eq userId
                            ReactionEntity::movie eq movieId
                            ReactionEntity::action eq action
                        }
                    }
                }.decodeSingleOrNull<ReactionEntity>()
        }
    }

    suspend fun createReaction(
        userId: String,
        movieId: Long,
        action: ReactionEntity.Action,
    ) {
        safeCall {
            supabaseClient.from(table = REACTION_TABLE)
                .insert(
                    InsertReaction(
                        user = userId,
                        movie = movieId,
                        action = action,
                    )
                )
        }
    }

    // MATCHED

    @OptIn(SupabaseExperimental::class)
    fun getMatchedListFlowByRoomId(roomId: String): Flow<List<MatchedEntity>> {
        return safeFlow {
            supabaseClient.from(table = MATCHED_TABLE)
                .selectAsFlow(
                    primaryKey = MatchedEntity::id,
                    filter = FilterOperation(
                        column = ROOM_COLUMN,
                        operator = FilterOperator.EQ,
                        value = roomId,
                    )
                )
        }
    }

    suspend fun createMatched(
        roomId: String,
        movieId: Long,
    ) {
        safeCall {
            supabaseClient.from(table = MATCHED_TABLE)
                .insert(
                    InsertMatched(
                        room = roomId,
                        movie = movieId,
                    )
                )
        }
    }


}
