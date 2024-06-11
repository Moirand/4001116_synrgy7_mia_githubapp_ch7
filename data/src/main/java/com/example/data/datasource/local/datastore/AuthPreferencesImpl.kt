package com.example.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.datastore.AuthPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthPreferencesImpl(
    private val datastore: DataStore<Preferences>
) : AuthPreferences {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val ID_KEY = intPreferencesKey("userId")
    }

    override suspend fun loadToken(): String? =
        datastore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull()
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
    override suspend fun loadUserId(): Int? =
        datastore.data.map { preferences ->
            preferences[ID_KEY]
        }.firstOrNull()
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