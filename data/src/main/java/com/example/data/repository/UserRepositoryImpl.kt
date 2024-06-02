package com.example.data.repository

import com.example.data.datasource.local.room.UserDao
import com.example.data.toUser
import com.example.domain.model.User
import com.example.domain.repository.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override suspend fun getUserById(id: Int): User? = userDao.getUserById(id)?.toUser()
    override suspend fun getUserIdByEmail(email: String): Int = userDao.getUserIdByEmail(email)
    override suspend fun updateFavoriteList(id: Int?, favoriteList: List<String>?) {
        val favoriteListString = favoriteList?.joinToString(",")
        userDao.updateFavoriteList(id, favoriteListString)
    }
    override suspend fun getFavoriteList(userId: Int?): List<String> {
        val favoriteListString = userDao.getFavoriteList(userId)
        return favoriteListString?.split(",")?.map { it.trim() } ?: listOf()
    }
}