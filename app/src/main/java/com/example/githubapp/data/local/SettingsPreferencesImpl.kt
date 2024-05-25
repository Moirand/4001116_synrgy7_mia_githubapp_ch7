package com.example.githubapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.githubapp.data.domain.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferencesImpl(private val datastore: DataStore<Preferences>) : SettingsPreferences {
    private val MODE_KEY = booleanPreferencesKey("mode")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val ID_KEY = intPreferencesKey("userId")

    override fun loadMode(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[MODE_KEY] ?: false
        }
    }

    override suspend fun saveMode(isDarkModeActive: Boolean) {
        datastore.edit { preferences ->
            preferences[MODE_KEY] = isDarkModeActive
        }
    }

    override fun loadToken(): Flow<String?> {
        return datastore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    override suspend fun saveToken(token: String) {
        datastore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        datastore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    override fun loadUserId(): Flow<Int> {
        return datastore.data.map { preferences ->
            preferences[ID_KEY] ?: -1
        }
    }

    override suspend fun saveUserId(id: Int) {
        datastore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }

    override suspend fun deleteUserId() {
        datastore.edit { preferences ->
            preferences.remove(ID_KEY)
        }
    }
}