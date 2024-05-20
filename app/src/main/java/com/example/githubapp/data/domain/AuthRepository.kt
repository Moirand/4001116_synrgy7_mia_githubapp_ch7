package com.example.githubapp.data.domain

import com.example.githubapp.data.local.AuthRepositoryImpl
import com.example.githubapp.data.local.room.UserDao
import com.example.githubapp.data.local.room.UserEntity

interface AuthRepository {
    suspend fun isEmailPasswordExist(email: String, password: String): Boolean
    suspend fun isUsernameExist(username: String): Boolean
    suspend fun isEmailExist(email: String): Boolean
    suspend fun insertUser(userEntity: UserEntity)
    suspend fun deleteUser(id: Int)

    companion object {
        @Volatile
        private var instance: AuthRepositoryImpl? = null
        fun getInstance(
            userDao: UserDao
        ): AuthRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: AuthRepositoryImpl(userDao)
            }.also { instance = it }
    }
}