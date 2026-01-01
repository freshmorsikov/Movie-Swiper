package com.github.freshmorsikov.moviematcher.core.data.api.supabase

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.CounterEntity
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

    suspend fun getRoomByCode(code: String): RoomEntity? {
        return supabaseClient.from(table = ROOM_TABLE)
            .select {
                filter { RoomEntity::code eq code }
            }.decodeSingleOrNull<RoomEntity>()
    }

    // USER

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

}