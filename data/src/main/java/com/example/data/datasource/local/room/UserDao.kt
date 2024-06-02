package com.example.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUserByUsernamePassword(email: String, password: String): UserEntity?

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT userId FROM user WHERE email = :email")
    suspend fun getUserIdByEmail(email: String): Int

    @Query("SELECT favoriteList FROM user WHERE userId = :userId")
    suspend fun getFavoriteList(userId: Int?): String?

    @Query("UPDATE user SET favoriteList = :favoriteList WHERE userId = :id")
    suspend fun updateFavoriteList(id: Int?, favoriteList: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE userId = :id")
    suspend fun deleteUser(id: Int)
}