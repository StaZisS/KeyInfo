package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.RequestRepository
import com.example.keyinfo.domain.model.request.Request
import retrofit2.HttpException

class GetRequestsUseCase {
    private val requestRepository = RequestRepository()

    suspend fun invoke(status: String): Result<ArrayList<Request>> {
        return try {
            val response = requestRepository.getRequests(status)
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