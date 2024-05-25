package com.example.githubapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT userId FROM user WHERE email = :email AND password = :password")
    suspend fun isEmailPasswordExist(email: String, password: String): Int?

    @Query("SELECT userId FROM user WHERE username = :username")
    suspend fun isUsernameExist(username: String): Int?

    @Query("SELECT userId FROM user WHERE email = :email")
    suspend fun isEmailExist(email: String): Int?

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: Int): UserEntity

    @Query("SELECT userId FROM user WHERE email = :email")
    suspend fun getUserIdByEmail(email: String): Int

    @Query("SELECT favoriteList FROM user WHERE userId = :userId")
    suspend fun getFavoriteList(userId: Int): String?

    @Query("UPDATE user SET favoriteList = :favoriteList WHERE userId = :id")
    suspend fun updateFavoriteList(id: Int, favoriteList: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE userId = :id")
    suspend fun deleteUser(id: Int)
}