package com.github.freshmorsikov.moviematcher.core.data.api.supabase

import com.github.freshmorsikov.moviematcher.core.data.api.safeCall
import com.github.freshmorsikov.moviematcher.core.data.api.safeFlow
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertMatched
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertReaction
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.MatchedEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.ReactionEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.UpdateMatchedActive
import com.github.freshmorsikov.moviematcher.feature.user.data.model.IncrementCounterResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecordOrNull
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.Json
import kotlin.random.Random

private const val MATCHED_TABLE = "matched"
private const val REACTION_TABLE = "reaction"
private const val INCREMENT_COUNTER_FUNCTION = "increment-counter"

private const val PUBLIC_SCHEMA = "public"
private const val ID_COLUMN = "id"
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
            val channel = supabaseClient.channel(matchedChannelName(roomId = roomId))
            val changes = channel.postgresChangeFlow<PostgresAction>(schema = PUBLIC_SCHEMA) {
                table = MATCHED_TABLE
            }
            flow {
                val cache = mutableMapOf<String, MatchedEntity>()
                val initialData = getMatchedListByRoomId(roomId = roomId)
                initialData.forEach { matched ->
                    cache[matched.cacheKey()] = matched
                }
                emit(cache.values.toList())
                channel.subscribe()
                changes.collect { action ->
                    cache.applyMatchedChange(
                        action = action,
                        roomId = roomId,
                    )
                    emit(cache.values.toList())
                }
            }.onCompletion {
                supabaseClient.realtime.removeChannel(channel)
            }
        }
    }

    private suspend fun getMatchedListByRoomId(roomId: String): List<MatchedEntity> {
        return supabaseClient.from(table = MATCHED_TABLE)
            .select {
                filter {
                    MatchedEntity::room eq roomId
                }
            }.decodeList<MatchedEntity>()
    }

    private fun MutableMap<String, MatchedEntity>.applyMatchedChange(
        action: PostgresAction,
        roomId: String,
    ) {
        when (action) {
            is PostgresAction.Insert -> {
                val matched = action.decodeRecordOrNull<MatchedEntity>() ?: return
                if (matched.room == roomId) {
                    this[matched.cacheKey()] = matched
                }
            }

            is PostgresAction.Update -> {
                val matched = action.decodeRecordOrNull<MatchedEntity>() ?: return
                if (matched.room == roomId) {
                    this[matched.cacheKey()] = matched
                } else {
                    removeAllById(id = matched.id)
                }
            }

            is PostgresAction.Delete -> removeDeletedMatched(action = action)

            else -> {}
        }
    }

    private fun MutableMap<String, MatchedEntity>.removeDeletedMatched(action: PostgresAction.Delete) {
        val id = action.oldRecord[ID_COLUMN]?.jsonPrimitive?.content ?: return
        val room = action.oldRecord[ROOM_COLUMN]?.jsonPrimitive?.content
        if (room == null) {
            removeAllById(id = id)
        } else {
            remove(matchedCacheKey(id = id, room = room))
        }
    }

    private fun MutableMap<String, MatchedEntity>.removeAllById(id: String) {
        entries.removeAll { (_, matched) ->
            matched.id == id
        }
    }

    private fun MatchedEntity.cacheKey(): String {
        return matchedCacheKey(
            id = id,
            room = room,
        )
    }

    private fun matchedCacheKey(
        id: String,
        room: String,
    ): String {
        return "$id:$room"
    }

    private fun matchedChannelName(roomId: String): String {
        return "$PUBLIC_SCHEMA:$MATCHED_TABLE:$roomId:${Random.nextLong()}"
    }

    suspend fun getMatched(
        roomId: String,
        movieId: Long,
    ): MatchedEntity? {
        return safeCall {
            supabaseClient.from(table = MATCHED_TABLE)
                .select {
                    filter {
                        and {
                            MatchedEntity::room eq roomId
                            MatchedEntity::movie eq movieId
                        }
                    }
                }.decodeList<MatchedEntity>()
                .firstOrNull()
        }
    }

    suspend fun createMatched(
        roomId: String,
        movieId: Long,
        active: Boolean,
    ) {
        safeCall {
            supabaseClient.from(table = MATCHED_TABLE)
                .insert(
                    InsertMatched(
                        room = roomId,
                        movie = movieId,
                        active = active,
                    )
                )
        }
    }

    suspend fun updateMatchedActive(
        roomId: String,
        movieId: Long,
        active: Boolean,
    ) {
        safeCall {
            supabaseClient.from(table = MATCHED_TABLE)
                .update(
                    value = UpdateMatchedActive(
                        active = active,
                    )
                ) {
                    filter {
                        and {
                            MatchedEntity::room eq roomId
                            MatchedEntity::movie eq movieId
                        }
                    }
                }
        }
    }

}
