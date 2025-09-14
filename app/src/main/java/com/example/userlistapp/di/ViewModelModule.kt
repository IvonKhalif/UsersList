package com.example.userlistapp.di

import com.example.userlistapp.presentation.ui.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { UsersViewModel(get()) }
}