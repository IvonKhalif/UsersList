package com.example.userlistapp.di

import org.koin.dsl.module

val AppModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
}