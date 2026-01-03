package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.api.supabase.SupabaseApiService
import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import com.github.freshmorsikov.moviematcher.shared.domain.model.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val USER_ID_KEY = "USER_ID_KEY"
private const val SHOW_PAIR_STATUS_KEY = "SHOW_PAIR_STATUS_KEY"

class UserRepository(
    private val supabaseApiService: SupabaseApiService,
    private val keyValueStore: KeyValueStore,
) {

    fun getRoomFlow(): Flow<Room?> {
        return keyValueStore.getStringFlow(USER_ID_KEY)
            .filterNotNull()
            .map { userId ->
                getRoomByUserId(userId = userId)
            }
    }

    suspend fun getRoom(): Room? {
        val userId = getUserId()
        return getRoomByUserId(userId = userId)
    }

    private suspend fun getRoomByUserId(userId: String): Room? {
        val user = supabaseApiService.getUserById(userId = userId) ?: return null
        val room = supabaseApiService.getRoomById(roomId = user.room) ?: return null
        return Room(
            id = room.id,
            code = room.code,
        )
    }

    suspend fun getUserIdOrNull(): String? {
        return keyValueStore.getString(USER_ID_KEY)
    }

    suspend fun getUserId(): String {
        return keyValueStore.getStringFlow(USER_ID_KEY)
            .filterNotNull()
            .first()
    }

    suspend fun getCodeCounter(): Long {
        return supabaseApiService.getCounter()?.value ?: 0L
    }

    suspend fun updateCodeCounter(counter: Long) {
        supabaseApiService.updateCounter(value = counter)
    }

    suspend fun createUser(code: String) {
        val roomId = supabaseApiService.createRoom(code = code).id
        val userId = supabaseApiService.createUser(roomId).id
        keyValueStore.putString(USER_ID_KEY, userId)
    }

    suspend fun updateUserCode(
        userId: String,
        code: String
    ): Boolean {
        val room = supabaseApiService.getRoomByCode(code = code) ?: return false

        supabaseApiService.updateUserRoom(
            userId = userId,
            roomId = room.id,
        )
        return true
    }

    suspend fun getShowPairStatus(): Boolean? {
        return keyValueStore.getBoolean(SHOW_PAIR_STATUS_KEY)
    }

    suspend fun saveShowPairStatus(showPairStatus: Boolean) {
        keyValueStore.putBoolean(SHOW_PAIR_STATUS_KEY, showPairStatus)
    }

}