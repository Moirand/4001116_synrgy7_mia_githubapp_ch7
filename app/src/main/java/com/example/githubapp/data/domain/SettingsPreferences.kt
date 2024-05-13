package com.example.githubapp.data.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.githubapp.data.local.SettingsPreferencesImpl
import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {
    fun getMode(): Flow<Boolean>
    suspend fun setMode(isDarkModeActive: Boolean)

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreferencesImpl? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreferencesImpl {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreferencesImpl(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}