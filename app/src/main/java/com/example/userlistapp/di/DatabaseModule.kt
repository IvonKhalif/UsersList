package com.example.userlistapp.di

import android.content.Context
import androidx.room.Room
import com.example.userlistapp.data.local.database.AppDatabase
import com.example.userlistapp.data.local.database.dao.UserDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideUserDao(get()) }
}

private fun provideDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideUserDao(database: AppDatabase): UserDao {
    return database.userDao()
}