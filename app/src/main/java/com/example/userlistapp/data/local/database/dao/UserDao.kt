package com.example.userlistapp.data.local.database.dao

import androidx.room.*
import com.example.userlistapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Transaction
    suspend fun refreshUsers(users: List<UserEntity>) {
        deleteAllUsers()
        insertUsers(users)
    }

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}