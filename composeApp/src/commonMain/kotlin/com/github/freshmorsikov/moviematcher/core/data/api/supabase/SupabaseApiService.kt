package com.github.freshmorsikov.moviematcher.core.data.api.supabase

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.CounterEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertMatched
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertRoom
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.InsertUser
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.MatchedEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.RoomEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.UserEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow

private const val COUNTER_TABLE = "counter"
private const val ROOM_TABLE = "room"
private const val USER_TABLE = "user"
private const val MATCHED_TABLE = "matched"

private const val ROOM_COLUMN = "room"

private const val COUNTER_ID = 1

class SupabaseApiService(
    private val supabaseClient: SupabaseClient
) {

    // COUNTER

    suspend fun getCounter(): CounterEntity? {
        return supabaseClient.from(table = COUNTER_TABLE)
            .select {
                filter { CounterEntity::id eq COUNTER_ID }
            }.decodeSingle<CounterEntity>()
    }

    suspend fun updateCounter(value: Long) {
        supabaseClient.from(COUNTER_TABLE).update(
            update = { CounterEntity::value setTo value }
        ) {
            filter { CounterEntity::id eq COUNTER_ID }
        }
    }

    // ROOM

    suspend fun getRoomById(roomId: String): RoomEntity? {
        return supabaseClient.from(table = ROOM_TABLE)
            .select {
                filter { RoomEntity::id eq roomId }
            }.decodeSingleOrNull<RoomEntity>()
    }

    suspend fun getRoomByCode(code: String): RoomEntity? {
        return supabaseClient.from(table = ROOM_TABLE)
            .select {
                filter { RoomEntity::code eq code }
            }.decodeSingleOrNull<RoomEntity>()
    }

    suspend fun createRoom(code: String): RoomEntity {
        return supabaseClient.from(table = ROOM_TABLE)
            .insert(
                value = InsertRoom(
                    code = code
                )
            ) {
                select()
            }.decodeSingle<RoomEntity>()
    }

    // USER

    suspend fun getUserById(userId: String): UserEntity? {
        return supabaseClient.from(table = USER_TABLE)
            .select {
                filter { UserEntity::id eq userId }
            }.decodeSingleOrNull<UserEntity>()
    }

    suspend fun updateUserRoom(
        userId: String,
        roomId: String
    ) {
        supabaseClient.from(USER_TABLE).update(
            update = { UserEntity::room setTo roomId }
        ) {
            filter { UserEntity::id eq userId }
        }
    }

    suspend fun createUser(roomId: String): UserEntity {
        return supabaseClient.from(table = USER_TABLE)
            .insert(
                value = InsertUser(
                    room = roomId
                )
            ) {
                select()
            }.decodeSingle<UserEntity>()
    }

    @OptIn(SupabaseExperimental::class)
    fun getUsersFlowByRoomId(roomId: String): Flow<List<UserEntity>> {
        return supabaseClient.from(table = USER_TABLE)
            .selectAsFlow(
                primaryKey = UserEntity::id,
                filter = FilterOperation(
                    column = ROOM_COLUMN,
                    operator = FilterOperator.EQ,
                    value = roomId,
                )
            )
    }

    // MATCHED

    @OptIn(SupabaseExperimental::class)
    fun getMatchedListFlowByRoomId(roomId: String): Flow<List<MatchedEntity>> {
        return supabaseClient.from(table = MATCHED_TABLE)
            .selectAsFlow(
                primaryKey = MatchedEntity::id,
                filter = FilterOperation(
                    column = ROOM_COLUMN,
                    operator = FilterOperator.EQ,
                    value = roomId,
                )
            )
    }

    suspend fun createMatched(
        roomId: String,
        movieId: Long,
    ) {
        supabaseClient.from(table = MATCHED_TABLE)
            .insert(
                InsertMatched(
                    room = roomId,
                    movie = movieId,
                )
            )
    }


}