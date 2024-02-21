package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import retrofit2.HttpException

class AcceptRequestUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(id: String): Result<Unit> {
        return try {
            val response = keysRepository.acceptRequest(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}