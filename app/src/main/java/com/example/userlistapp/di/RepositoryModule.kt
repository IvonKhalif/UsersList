package com.example.userlistapp.di

import com.example.userlistapp.data.repository.UserRepositoryImpl
import com.example.userlistapp.domain.repository.UserRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<UserRepository> {
        val dispatcherProvider: DispatcherProvider = get()
        UserRepositoryImpl(
            apiService = get(),
            userDao = get(),
            ioDispatcher = dispatcherProvider.io
        )
    }
}