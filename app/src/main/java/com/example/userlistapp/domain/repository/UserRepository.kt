package com.example.userlistapp.domain.repository

import com.example.userlistapp.domain.model.User
import com.example.userlistapp.presentation.state.UiState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<UiState<List<User>>>
    suspend fun refreshUsers(): Result<Unit>
}