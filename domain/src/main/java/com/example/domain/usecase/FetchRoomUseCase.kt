package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository

class FetchRoomUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend fun getUserIdByEmail(email: String): Int =
        userRepository.getUserIdByEmail(email)

    suspend fun getUserByEmailPassword(email: String, password: String): User? =
        authRepository.getUserByEmailPassword(email, password)

    suspend fun getUserByUsername(username: String): User? =
        authRepository.getUserByUsername(username)

    suspend fun getUserByEmail(email: String): User? =
        authRepository.getUserByEmail(email)

    suspend fun getUserById(id: Int): User? =
        userRepository.getUserById(id)

    suspend fun getFavoriteList(userId: Int?): List<String> =
        userRepository.getFavoriteList(userId)
}