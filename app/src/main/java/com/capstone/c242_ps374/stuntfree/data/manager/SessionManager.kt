package com.capstone.c242_ps374.stuntfree.data.manager

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_FIRST_TIME = "key_first_time" // Tambahkan key baru
    }

    init {
        if (!sharedPreferences.contains(KEY_FIRST_TIME)) {
            sharedPreferences.edit()
                .putBoolean(KEY_FIRST_TIME, true)
                .apply()
        }
    }

    fun saveAuthToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, token)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clearSession() {
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_IS_LOGGED_IN)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun isFirstTime(): Boolean {
        val isFirst = sharedPreferences.getBoolean(KEY_FIRST_TIME, true)
        if (isFirst) {
            sharedPreferences.edit()
                .putBoolean(KEY_FIRST_TIME, false)
                .apply()
        }
        return isFirst
    }
}