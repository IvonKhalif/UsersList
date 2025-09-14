package com.example.userlistapp.data.repository

import com.example.userlistapp.data.local.database.dao.UserDao
import com.example.userlistapp.data.local.entity.UserEntity
import com.example.userlistapp.data.remote.api.ApiService
import com.example.userlistapp.data.remote.dto.UserDto
import com.example.userlistapp.domain.model.User
import com.example.userlistapp.domain.repository.UserRepository
import com.example.userlistapp.presentation.state.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override fun getUsers(): Flow<UiState<List<User>>> = flow {
        emit(UiState.Loading)

        userDao.getAllUsers()
            .map { entities ->
                if (entities.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(entities.map { it.toDomainModel() })
                }
            }
            .catch { e ->
                emit(UiState.Error("Error membaca data lokal: ${e.message}"))
            }
            .collect { state ->
                emit(state)
            }

        refreshUsers()
            .onFailure { exception ->
                val cachedCount = userDao.getUserCount()
                if (cachedCount == 0) {
                    emit(UiState.Error(getErrorMessage(exception)))
                }
            }
    }.flowOn(ioDispatcher)

    override suspend fun refreshUsers(): Result<Unit> = withContext(ioDispatcher) {
        try {
            val remoteUsers = apiService.getUsers()
            val userEntities = remoteUsers.map { it.toEntity() }
            userDao.refreshUsers(userEntities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is IOException -> "Tidak ada koneksi internet"
            is SocketTimeoutException -> "Koneksi timeout, coba lagi"
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    404 -> "Data tidak ditemukan"
                    500 -> "Server error, coba beberapa saat lagi"
                    else -> "Error: ${exception.message()}"
                }
            }
            else -> "Terjadi kesalahan: ${exception.message}"
        }
    }
}

// Extension functions for mapping
private fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email
    )
}

private fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        name = name,
        email = email
    )
}