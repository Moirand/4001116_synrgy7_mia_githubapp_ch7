package com.example.domain.datastore

interface AuthPreferences {
    suspend fun loadToken(): String?
    suspend fun saveToken(token: String)
    suspend fun deleteToken()
    suspend fun loadUserId(): Int?
    suspend fun saveUserId(id: Int)
    suspend fun deleteUserId()
}