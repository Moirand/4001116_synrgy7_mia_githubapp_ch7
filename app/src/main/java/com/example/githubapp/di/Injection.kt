package com.example.githubapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.githubapp.data.domain.AuthRepository
import com.example.githubapp.data.domain.SettingsPreferences
import com.example.githubapp.data.domain.UserRepository
import com.example.githubapp.data.local.room.UserDatabase

object Injection {
    fun provideSettingsPreferences(datastore: DataStore<Preferences>): SettingsPreferences {
        return SettingsPreferences.getInstance(datastore)
    }
    fun provideUserRepository(context: Context): UserRepository {
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(dao)
    }
    fun provideAuthRepository(context: Context): AuthRepository {
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return AuthRepository.getInstance(dao)
    }
}