package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.repository.AuthenticationRepository
import com.example.keyinfo.domain.model.authorization.Login

class PostLoginUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke(login: Login) : Result<TokenResponse?> {
        val response = authenticationRepository.postLogin(login)

        return try {
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}