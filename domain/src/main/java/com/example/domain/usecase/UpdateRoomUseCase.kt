package com.example.domain.usecase

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository

class UpdateRoomUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend fun insertUser(userEntity: User) {
        authRepository.insertUser(userEntity)
    }

    suspend fun deleteUser(id: Int) =
        authRepository.deleteUser(id)

    suspend fun updateFavoriteList(id: Int?, favoriteList: List<String>?) {
        userRepository.updateFavoriteList(id, favoriteList)
    }
}