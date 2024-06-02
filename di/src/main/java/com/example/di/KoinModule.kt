package com.example.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.domain.datastore.AuthPreferences
import com.example.domain.datastore.SettingsPreferences
import com.example.data.datasource.local.datastore.AuthPreferencesImpl
import com.example.data.datasource.local.datastore.SettingsPreferencesImpl
import com.example.data.datasource.local.datastore.datastore
import com.example.data.datasource.local.room.UserDao
import com.example.data.datasource.local.room.UserDatabase
import com.example.data.datasource.remote.retrofit.ApiConfig
import com.example.data.datasource.remote.retrofit.ApiService
import com.example.data.repository.ApiRepositoryImpl
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.ApiRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.FetchApiUseCase
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import com.example.domain.usecase.UpdateRoomUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    single<FetchApiUseCase> { FetchApiUseCase(get()) }
    single<FetchPreferencesUseCase> { FetchPreferencesUseCase(get(), get()) }
    single<FetchRoomUseCase> { FetchRoomUseCase(get(), get()) }
    single<UpdatePreferencesUseCase> { UpdatePreferencesUseCase(get(), get()) }
    single<UpdateRoomUseCase> { UpdateRoomUseCase(get(), get()) }

    single<ApiRepository> { ApiRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<AuthPreferences> { AuthPreferencesImpl(get()) }
    single<SettingsPreferences> { SettingsPreferencesImpl(get()) }

    single<UserDao> { (get() as UserDatabase).userDao() }
    single<ApiService> { ApiConfig.provideApiService(get()) }
    single<UserDatabase> {
        Room.databaseBuilder(
            context = get(),
            name = UserDatabase.DATABASE_NAME,
            klass = UserDatabase::class.java
        ).build()
    }
    single<DataStore<Preferences>> { androidContext().datastore }
}