package com.example.domain.repository

import com.example.domain.model.User

interface AuthRepository {
    suspend fun getUserByEmailPassword(email: String, password: String): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(userEntity: User)
    suspend fun deleteUser(id: Int)
}