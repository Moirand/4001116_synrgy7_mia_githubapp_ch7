package com.example.data.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "User.db"
    }
    abstract fun userDao(): UserDao
}