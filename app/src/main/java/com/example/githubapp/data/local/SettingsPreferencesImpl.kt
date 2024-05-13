package com.example.githubapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.githubapp.data.domain.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferencesImpl(private val datastore: DataStore<Preferences>) : SettingsPreferences {
    private val MODE_KEY = booleanPreferencesKey("mode")

    override fun getMode(): Flow<Boolean> {
        return datastore.data.map {
            it[MODE_KEY] ?: false
        }
    }

    override suspend fun setMode(isDarkModeActive: Boolean) {
        datastore.edit { preferences ->
            preferences[MODE_KEY] = isDarkModeActive
        }
    }

}