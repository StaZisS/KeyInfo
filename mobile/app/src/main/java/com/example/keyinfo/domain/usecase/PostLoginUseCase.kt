package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.repository.AuthenticationRepository
import com.example.keyinfo.domain.model.authorization.Login
import com.example.keyinfo.domain.model.authorization.Registration
import retrofit2.HttpException

class PostLoginUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke(login: Login): Result<TokenResponse> {
        return try {
            val response = authenticationRepository.postLogin(login)
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