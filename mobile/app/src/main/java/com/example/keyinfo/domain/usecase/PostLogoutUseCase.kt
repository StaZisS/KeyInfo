package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.AuthenticationRepository
import retrofit2.HttpException

class PostLogoutUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke() : Result<Unit?> {
        val response = authenticationRepository.postLogout()

        return try {
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}