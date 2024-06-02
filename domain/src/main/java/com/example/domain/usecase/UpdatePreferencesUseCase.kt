package com.example.domain.usecase

import com.example.domain.datastore.AuthPreferences
import com.example.domain.datastore.SettingsPreferences

class UpdatePreferencesUseCase(
    private val authPreferences: AuthPreferences,
    private val settingsPreferences: SettingsPreferences
) {
    suspend fun saveMode(mode: Boolean) =
        settingsPreferences.saveMode(mode)

    suspend fun saveToken(token: String) =
        authPreferences.saveToken(token)

    suspend fun deleteToken() =
        authPreferences.deleteToken()

    suspend fun saveUserId(userId: Int) =
        authPreferences.saveUserId(userId)

    suspend fun deleteUserId() =
        authPreferences.deleteUserId()
}