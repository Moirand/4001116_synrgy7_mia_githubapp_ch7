package com.example.githubapp.data.local

import com.example.githubapp.data.domain.AuthRepository
import com.example.githubapp.data.local.room.UserDao
import com.example.githubapp.data.local.room.UserEntity

class AuthRepositoryImpl(private val userDao: UserDao) : AuthRepository {
    override suspend fun isEmailPasswordExist(email: String, password: String): Boolean {
        return userDao.isEmailPasswordExist(email, password) != null
    }

    override suspend fun isUsernameExist(username: String): Boolean {
        return userDao.isUsernameExist(username) != null
    }

    override suspend fun isEmailExist(email: String): Boolean {
        return userDao.isEmailExist(email) != null
    }

    override suspend fun insertUser(userEntity: UserEntity) {
        userDao.insertUser(userEntity)
    }

    override suspend fun deleteUser(id: Int) {
        userDao.deleteUser(id)
    }
}