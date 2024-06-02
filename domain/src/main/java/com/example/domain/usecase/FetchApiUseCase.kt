package com.example.domain.usecase

import com.example.domain.model.ApiDetailUser
import com.example.domain.model.ApiSearchUser
import com.example.domain.model.ApiUser
import com.example.domain.repository.ApiRepository

class FetchApiUseCase(private val apiRepository: ApiRepository) {
    suspend fun getAllUsers(): List<ApiUser> =
        apiRepository.getAllUsers()

    suspend fun getSearchedUsername(username: String): ApiSearchUser =
        apiRepository.getSearchedUsername(username)

    suspend fun getDetailUser(username: String): ApiDetailUser =
        apiRepository.getDetailUser(username)

    suspend fun getFollowers(username: String): List<ApiUser> =
        apiRepository.getFollowers(username)

    suspend fun getFollowing(username: String): List<ApiUser> =
        apiRepository.getFollowers(username)
}