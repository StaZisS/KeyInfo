package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.repository.RefreshTokenRepository
import com.example.keyinfo.domain.model.RefreshToken
import retrofit2.HttpException


class RefreshTokenUseCase {
    private val refreshTokenRepository = RefreshTokenRepository()
    suspend fun invoke(refreshToken: RefreshToken): Result<TokenResponse> {
        return try {
            val response = refreshTokenRepository.getNewToken(refreshToken)
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