package com.example.keyinfo.domain.usecase

import android.util.Log
import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.repository.AuthenticationRepository
import com.example.keyinfo.domain.model.authorization.Registration

class PostRegistrationUseCase {
    private val authenticationRepository = AuthenticationRepository()

    suspend fun invoke(registration: Registration) : Result<TokenResponse?> {
        val response = authenticationRepository.postRegistration(registration)
        return if (response.isSuccessful) {
            Result.success(response.body())
        } else {
            Log.d("Error", "Error: ${response.code()}")
            Result.failure(Exception("Error: ${response.code()}"))
        }
    }

}