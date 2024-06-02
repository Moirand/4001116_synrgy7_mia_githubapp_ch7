package com.example.domain.repository

import com.example.domain.model.ApiDetailUser
import com.example.domain.model.ApiSearchUser
import com.example.domain.model.ApiUser

interface ApiRepository {
    suspend fun getAllUsers(): List<ApiUser>
    suspend fun getSearchedUsername(username: String): ApiSearchUser
    suspend fun getDetailUser(username: String): ApiDetailUser
    suspend fun getFollowers(username: String): List<ApiUser>
    suspend fun getFollowing(username: String): List<ApiUser>
}