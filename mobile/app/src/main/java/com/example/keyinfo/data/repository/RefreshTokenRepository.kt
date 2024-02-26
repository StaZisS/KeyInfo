package com.example.keyinfo.data.repository

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.RefreshToken
import retrofit2.Response

class RefreshTokenRepository {
    suspend fun getNewToken(refresh: RefreshToken): Response<TokenResponse> {
        return NetworkService.refreshTokenApiService.getNewToken(refresh)
    }

    suspend fun getNewRefreshToken(refresh: RefreshToken): Response<TokenResponse> {
        return NetworkService.refreshTokenApiService.getNewRefreshToken(refresh)
    }
}