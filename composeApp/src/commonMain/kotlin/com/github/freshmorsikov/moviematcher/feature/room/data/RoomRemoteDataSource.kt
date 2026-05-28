package com.github.freshmorsikov.moviematcher.feature.room.data

import com.github.freshmorsikov.moviematcher.core.data.api.safeCall
import com.github.freshmorsikov.moviematcher.core.data.api.safeFlow
import com.github.freshmorsikov.moviematcher.feature.room.data.model.InsertRoom
import com.github.freshmorsikov.moviematcher.feature.room.data.model.RoomEntity
import com.github.freshmorsikov.moviematcher.feature.room.data.model.UpdateRoomGenreFilter
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow

private const val ROOM_TABLE = "room"

class RoomRemoteDataSource(
    private val supabaseClient: SupabaseClient
) {

    @OptIn(SupabaseExperimental::class)
    fun getRoomFlowById(roomId: String): Flow<RoomEntity?> {
        return safeFlow {
            supabaseClient.from(table = ROOM_TABLE)
                .selectSingleValueAsFlow(RoomEntity::id) {
                    RoomEntity::id eq roomId
                }
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

    suspend fun updateRoomGenreFilter(
        roomId: String,
        genreFilter: List<Long>,
    ) {
        safeCall {
            supabaseClient.from(table = ROOM_TABLE)
                .update(
                    value = UpdateRoomGenreFilter(
                        genreFilter = genreFilter,
                    )
                ) {
                    filter { RoomEntity::id eq roomId }
                }
        }
    }

}
