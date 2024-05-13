package com.example.githubapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.githubapp.data.domain.SettingsPreferences

object Injection {
    fun provideSettingsPreferences(datastore: DataStore<Preferences>): SettingsPreferences {
        return SettingsPreferences.getInstance(datastore)
    }
}