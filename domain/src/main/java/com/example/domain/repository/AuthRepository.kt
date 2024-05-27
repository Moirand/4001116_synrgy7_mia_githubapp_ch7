package com.example.domain.repository

import com.example.domain.model.User

interface AuthRepository {
    suspend fun isEmailPasswordExist(email: String, password: String): Boolean
    suspend fun isUsernameExist(username: String): Boolean
    suspend fun isEmailExist(email: String): Boolean
    suspend fun insertUser(userEntity: User)
    suspend fun deleteUser(id: Int)
}