package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun getUserById(id: Int): User?
    suspend fun getUserIdByEmail(email: String): Int
    suspend fun updateFavoriteList(id: Int?, favoriteList: List<String>?)
    suspend fun getFavoriteList(userId: Int?): List<String>
}