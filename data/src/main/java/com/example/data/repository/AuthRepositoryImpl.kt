package com.example.data.repository

import com.example.data.datasource.local.room.UserDao
import com.example.data.toUser
import com.example.data.toUserEntity
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl(private val userDao: UserDao) : AuthRepository {
    override suspend fun getUserByEmailPassword(email: String, password: String): User? =
        userDao.getUserByUsernamePassword(email, password)?.toUser()

    override suspend fun getUserByUsername(username: String): User? =
        userDao.getUserByUsername(username)?.toUser()

    override suspend fun getUserByEmail(email: String): User? =
        userDao.getUserByEmail(email)?.toUser()

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toUserEntity())
    }

    override suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }
}