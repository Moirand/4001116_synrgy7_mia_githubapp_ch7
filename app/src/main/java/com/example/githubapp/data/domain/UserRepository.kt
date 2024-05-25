package com.example.githubapp.data.domain

import com.example.githubapp.data.local.UserRepositoryImpl
import com.example.githubapp.data.local.room.UserDao
import com.example.githubapp.data.local.room.UserEntity

interface UserRepository {
    suspend fun getUserById(id: Int): UserEntity
    suspend fun getUserIdByEmail(email: String): Int
    suspend fun updateFavoriteList(id: Int, favoriteList: List<String>?)
    suspend fun getFavoriteList(userId: Int): List<String>

    companion object {
        @Volatile
        private var instance: UserRepositoryImpl? = null
        fun getInstance(
            userDao: UserDao
        ): UserRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: UserRepositoryImpl(userDao)
            }.also { instance = it }
    }
}