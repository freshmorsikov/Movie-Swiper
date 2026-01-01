package com.github.freshmorsikov.moviematcher.core.data.api.supabase

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.RoomEntity
import com.github.freshmorsikov.moviematcher.core.data.api.supabase.model.UserEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow

private const val ROOM_TABLE = "room"
private const val USER_TABLE = "user"

private const val ROOM_COLUMN = "room"

class SupabaseApiService(
    private val supabaseClient: SupabaseClient
) {

    suspend fun getRoomByCode(code: String): RoomEntity? {
        return supabaseClient.from(table = ROOM_TABLE)
            .select {
                filter { RoomEntity::code eq code }
            }.decodeSingleOrNull<RoomEntity>()
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

}