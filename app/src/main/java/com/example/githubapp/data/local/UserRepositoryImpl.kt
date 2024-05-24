package com.example.githubapp.data.local

import com.example.githubapp.data.domain.UserRepository
import com.example.githubapp.data.local.room.UserDao
import com.example.githubapp.data.local.room.UserEntity

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun getUserById(id: Int): UserEntity {
        return userDao.getUserById(id)
    }

    override suspend fun getUserIdByEmail(email: String): Int {
        return userDao.getUserIdByEmail(email)
    }

    override suspend fun updateFavoriteList(id: Int, favoriteList: List<String>?) {
        val favoriteListString = favoriteList?.joinToString(",")
        userDao.updateFavoriteList(id, favoriteListString)
    }

    override suspend fun getFavoriteList(userId: Int): List<String> {
        val favoriteListString = userDao.getFavoriteList(userId)
        return favoriteListString?.split(",")?.map { it.trim() } ?: listOf()
    }
}