package com.github.freshmorsikov.moviematcher.shared.data

import com.github.freshmorsikov.moviematcher.core.data.local.KeyValueStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val USER_UUID_KEY = "USER_UUID_KEY"
private const val SHOW_PAIR_STATUS_KEY = "SHOW_PAIR_STATUS_KEY"

class UserRepository(
    private val keyValueStore: KeyValueStore
) {

    suspend fun getUserUuid(): String? {
        return keyValueStore.getString(USER_UUID_KEY)
    }

    suspend fun saveUserUuid(uuid: String) {
        keyValueStore.putString(USER_UUID_KEY, uuid)
    }

    suspend fun getCodeCounter(): Long {
        val snapshot = Firebase.database.reference("counter").valueEvents.firstOrNull()

        return (snapshot?.value as? Long) ?: 0L
    }

    suspend fun updateCodeCounter(counter: Long) {
        Firebase.database.reference("counter").setValue(counter)
    }

    suspend fun getUserCode(userUuid: String): String? {
        val snapshot = Firebase.database.reference()
            .child("users")
            .child(userUuid)
            .child("code")
            .valueEvents
            .firstOrNull()
        return snapshot?.value as? String
    }

    fun getUserCodeFlow(userUuid: String): Flow<String?> {
        val reference = Firebase.database.reference()
            .child("users")
            .child(userUuid)
            .child("code")
        return reference.valueEvents.map { snapshot ->
            snapshot.value as? String
        }
    }

    suspend fun saveUserCode(userUuid: String, code: String) {
        val reference = Firebase.database.reference()
            .child("users")
            .child(userUuid)
            .child("code")
        reference.setValue(code)
    }

    suspend fun getShowPairStatus(): Boolean? {
        return keyValueStore.getBoolean(SHOW_PAIR_STATUS_KEY)
    }

    suspend fun saveShowPairStatus(showPairStatus: Boolean) {
        keyValueStore.putBoolean(SHOW_PAIR_STATUS_KEY, showPairStatus)
    }

}