package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.RequestRepository
import retrofit2.HttpException

class DeleteRequestUseCase {
    private val requestRepository = RequestRepository()

    suspend fun invoke(id: String): Result<Unit> {
        return try {
            val response = requestRepository.deleteRequest(id)
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