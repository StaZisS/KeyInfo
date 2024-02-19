package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import com.example.keyinfo.domain.model.keys.User
import retrofit2.HttpException

class GetAllUsersUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(): Result<ArrayList<User>> {
        return try {
            val response = keysRepository.getAllUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}