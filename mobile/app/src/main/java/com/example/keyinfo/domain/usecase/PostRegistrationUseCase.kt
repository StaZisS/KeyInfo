package com.example.keyinfo.domain.usecase

import android.util.Log
import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.repository.AuthenticationRepository
import com.example.keyinfo.domain.model.authorization.Registration
import retrofit2.HttpException

class PostRegistrationUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke(registration: Registration): Result<TokenResponse> {
        return try {
            val response = authenticationRepository.postRegistration(registration)
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
