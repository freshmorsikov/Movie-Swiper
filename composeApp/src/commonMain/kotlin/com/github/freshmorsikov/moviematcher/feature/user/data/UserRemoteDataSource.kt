package com.github.freshmorsikov.moviematcher.feature.user.data

import com.github.freshmorsikov.moviematcher.core.data.api.safeCall
import com.github.freshmorsikov.moviematcher.core.data.api.safeFlow
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow

private const val USER_TABLE = "user"
private const val ROOM_COLUMN = "room"

class UserRemoteDataSource(
    private val supabaseClient: SupabaseClient
) {

    suspend fun getUserById(userId: String): UserEntity? {
        return safeCall {
            supabaseClient.from(table = USER_TABLE)
                .select {
                    filter { UserEntity::id eq userId }
                }.decodeSingleOrNull<UserEntity>()
        }
    }

    @OptIn(SupabaseExperimental::class)
    fun getUserFlowById(userId: String): Flow<UserEntity?> {
        return safeFlow {
            supabaseClient.from(table = USER_TABLE)
                .selectSingleValueAsFlow(UserEntity::id) {
                    UserEntity::id eq userId
                }
        }
    }

    suspend fun updateUserRoom(
        userId: String,
        roomId: String
    ) {
        safeCall {
            supabaseClient.from(USER_TABLE).update(
                update = { UserEntity::room setTo roomId }
            ) {
                filter { UserEntity::id eq userId }
            }
        }
    }

    suspend fun updateUserName(
        userId: String,
        name: String,
    ) {
        safeCall {
            supabaseClient.from(USER_TABLE).update(
                update = { UserEntity::name setTo name }
            ) {
                filter { UserEntity::id eq userId }
            }
        }
    }

    suspend fun createUser(
        roomId: String,
        name: String,
    ): UserEntity? {
        return safeCall {
            supabaseClient.from(table = USER_TABLE)
                .insert(
                    value = InsertUser(
                        room = roomId,
                        name = name,
                    )
                ) {
                    select()
                }.decodeSingle<UserEntity>()
        }
    }

    @OptIn(SupabaseExperimental::class)
    fun getUsersFlowByRoomId(roomId: String): Flow<List<UserEntity>> {
        return safeFlow {
            supabaseClient.from(table = USER_TABLE)
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

    @OptIn(SupabaseExperimental::class)
    suspend fun getUsersByRoomId(roomId: String): List<UserEntity> {
        return safeCall {
            supabaseClient.from(table = USER_TABLE)
                .select {
                    filter {
                        UserEntity::room eq roomId
                    }
                }.decodeList<UserEntity>()
        }.orEmpty()
    }

}