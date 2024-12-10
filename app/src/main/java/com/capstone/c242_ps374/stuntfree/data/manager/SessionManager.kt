package com.capstone.c242_ps374.stuntfree.data.manager

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = "user_preferences")

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_TOKEN_TYPE = stringPreferencesKey("token_type")
        private val KEY_STAGE = stringPreferencesKey("stage")
        private val KEY_FIRST_TIME = booleanPreferencesKey("key_first_time")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveAuthToken(tokenType: String, accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN_TYPE] = tokenType
            preferences[KEY_ACCESS_TOKEN] = accessToken
            preferences[KEY_REFRESH_TOKEN] = refreshToken
            preferences[KEY_IS_LOGGED_IN] = true
        }
    }


    fun getBearerToken(): Flow<String?> = context.dataStore.data.map { preferences ->
        val accessToken = preferences[KEY_ACCESS_TOKEN]
        val refreshToken = preferences[KEY_REFRESH_TOKEN]
        val tokenType = preferences[KEY_TOKEN_TYPE] ?: "bearer"
        "$tokenType $accessToken $refreshToken"
    }

    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[KEY_ACCESS_TOKEN] }
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[KEY_REFRESH_TOKEN] }
    fun getTokenType(): Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN_TYPE]}

    suspend fun saveStage(stage: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_STAGE] = stage
        }
    }

    fun getStage(): Flow<String?> = context.dataStore.data.map { it[KEY_STAGE] }

    // Ini untuk nanti (progress)
//    suspend fun clearSession() {
//        context.dataStore.edit { preferences ->
//            preferences.remove(KEY_FIRST_TIME)
//            preferences.remove(KEY_IS_LOGGED_IN)
//            preferences.remove(KEY_TOKEN_TYPE)
//            preferences.remove(KEY_ACCESS_TOKEN)
//            preferences.remove(KEY_REFRESH_TOKEN)
//        }
//    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isFirstTime(): Flow<Boolean> = context.dataStore.data.map { it[KEY_FIRST_TIME] ?: false }

    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }
}