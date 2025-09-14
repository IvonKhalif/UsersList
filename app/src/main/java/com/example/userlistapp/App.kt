package com.example.userlistapp

import android.app.Application
import com.example.userlistapp.di.AppModule
import com.example.userlistapp.di.DatabaseModule
import com.example.userlistapp.di.NetworkModule
import com.example.userlistapp.di.RepositoryModule
import com.example.userlistapp.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                AppModule,
                NetworkModule,
                DatabaseModule,
                RepositoryModule,
                ViewModelModule
            )
        }
    }
}