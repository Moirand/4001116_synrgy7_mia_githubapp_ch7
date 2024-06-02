package com.example.domain.datastore


interface SettingsPreferences {
    suspend fun loadMode(): Boolean
    suspend fun saveMode(isDarkModeActive: Boolean)
}