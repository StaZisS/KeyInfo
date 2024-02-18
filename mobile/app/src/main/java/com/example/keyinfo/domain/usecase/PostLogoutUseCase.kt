package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.AuthenticationRepository
import com.example.keyinfo.domain.model.RefreshToken
import retrofit2.HttpException

class PostLogoutUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke(refresh: String) : Result<Unit?> {
        val body = RefreshToken(refresh)
        val response = authenticationRepository.postLogout(body)

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