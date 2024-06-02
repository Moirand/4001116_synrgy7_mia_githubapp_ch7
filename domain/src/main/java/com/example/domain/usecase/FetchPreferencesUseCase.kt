package com.example.domain.usecase

import com.example.domain.datastore.AuthPreferences
import com.example.domain.datastore.SettingsPreferences

class FetchPreferencesUseCase(
    private val authPreferences: AuthPreferences,
    private val settingsPreferences: SettingsPreferences
) {
    suspend fun loadMode(): Boolean = settingsPreferences.loadMode()
    suspend fun loadToken(): String? = authPreferences.loadToken()
    suspend fun loadUserId(): Int? = authPreferences.loadUserId()
}