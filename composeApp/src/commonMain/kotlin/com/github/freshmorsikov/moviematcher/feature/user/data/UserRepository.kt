package com.github.freshmorsikov.moviematcher.feature.user.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import com.github.freshmorsikov.moviematcher.feature.user.data.mapper.toUser
import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room
import com.github.freshmorsikov.moviematcher.feature.user.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

private const val USER_ID_KEY = "USER_ID_KEY"
private const val USER_NAME_KEY = "USER_NAME_KEY"
private const val ROOM_CODE_KEY = "ROOM_CODE_KEY"
private const val SHOW_PAIR_STATUS_KEY = "SHOW_PAIR_STATUS_KEY"

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val supabaseApiService: SupabaseApiService,
    private val keyValueStore: KeyValueStore,
) {

    fun getPairedFlow(): Flow<Boolean> {
        return getRoomFlow()
            .filterNotNull()
            .flatMapLatest { room ->
                userRemoteDataSource.getUsersFlowByRoomId(roomId = room.id).map { users ->
                    users.size > 1
                }
            }
    }

    fun getRoomFlow(): Flow<Room> {
        return keyValueStore.getStringFlow(USER_ID_KEY)
            .filterNotNull()
            .flatMapLatest { userId ->
                getRoomFlowByUserId(userId = userId)
            }
    }

    fun getPairedUserFlow(): Flow<User?> {
        return keyValueStore.getStringFlow(USER_ID_KEY)
            .filterNotNull()
            .flatMapLatest { userId ->
                getRoomFlowByUserId(userId = userId)
                    .flatMapLatest { room ->
                        userRemoteDataSource.getUsersFlowByRoomId(roomId = room.id)
                            .map { users ->
                                users.firstOrNull { user -> user.id != userId }?.toUser()
                            }
                    }
            }
    }

    private suspend fun getRoomByUserId(userId: String): Room? {
        val user = userRemoteDataSource.getUserById(userId = userId) ?: return null
        val room = supabaseApiService.getRoomById(roomId = user.room) ?: return null
        return Room(
            id = room.id,
            code = room.code,
        )
    }

    private suspend fun getRoomFlowByUserId(userId: String): Flow<Room> {
        return userRemoteDataSource.getUserFlowById(userId = userId)
            .filterNotNull()
            .mapNotNull { user ->
                val room = supabaseApiService.getRoomById(roomId = user.room) ?: return@mapNotNull null
                Room(
                    id = room.id,
                    code = room.code,
                )
            }
    }

    suspend fun getUserIdOrNull(): String? {
        return keyValueStore.getString(USER_ID_KEY)
    }

    suspend fun getUserId(): String {
        return keyValueStore.getStringFlow(USER_ID_KEY)
            .filterNotNull()
            .first()
    }

    suspend fun getUserNameOrNull(): String? {
        return keyValueStore.getString(USER_NAME_KEY)
    }

    suspend fun getRoomCodeOrNull(): String? {
        return keyValueStore.getString(ROOM_CODE_KEY)
    }

    suspend fun getCodeCounter(): Long {
        return supabaseApiService.getCounter()?.value ?: 0L
    }

    suspend fun getPairedUser(): User? {
        val userId = getUserId()
        val room = getRoomByUserId(userId = userId) ?: return null
        val users = userRemoteDataSource.getUsersByRoomId(roomId = room.id)

        return users.find { user ->
            user.id != userId
        }?.toUser()
    }

    suspend fun updateCodeCounter(counter: Long) {
        supabaseApiService.updateCounter(value = counter)
    }

    suspend fun createUser(
        code: String,
        name: String,
    ) {
        val roomId = supabaseApiService.createRoom(code = code)?.id ?: return
        val userId = userRemoteDataSource.createUser(
            roomId = roomId,
            name = name
        )?.id ?: return

        keyValueStore.putString(USER_ID_KEY, userId)
        keyValueStore.putString(USER_NAME_KEY, name)
        keyValueStore.putString(ROOM_CODE_KEY, code)
    }

    suspend fun updateUserName(
        userId: String,
        name: String,
    ) {
        userRemoteDataSource.updateUserName(
            userId = userId,
            name = name,
        )
        keyValueStore.putString(USER_NAME_KEY, name)
    }

    suspend fun updateRoom(
        userId: String,
        code: String
    ): Boolean {
        val room = supabaseApiService.getRoomByCode(code = code) ?: return false

        userRemoteDataSource.updateUserRoom(
            userId = userId,
            roomId = room.id,
        )
        keyValueStore.putString(ROOM_CODE_KEY, code)
        return true
    }

    suspend fun getShowPairStatus(): Boolean? {
        return keyValueStore.getBoolean(SHOW_PAIR_STATUS_KEY)
    }

    suspend fun saveShowPairStatus(showPairStatus: Boolean) {
        keyValueStore.putBoolean(SHOW_PAIR_STATUS_KEY, showPairStatus)
    }

}
