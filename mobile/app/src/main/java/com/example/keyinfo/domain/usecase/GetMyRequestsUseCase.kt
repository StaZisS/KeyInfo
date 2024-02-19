package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import com.example.keyinfo.domain.model.keys.Transfer
import retrofit2.HttpException

class GetMyRequestsUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(status: String): Result<ArrayList<Transfer>> {
        return try {
            val response = keysRepository.getMyRequests(status)
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