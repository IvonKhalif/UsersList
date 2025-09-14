package com.example.userlistapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userlistapp.data.local.database.dao.UserDao
import com.example.userlistapp.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "users_database"
    }
}