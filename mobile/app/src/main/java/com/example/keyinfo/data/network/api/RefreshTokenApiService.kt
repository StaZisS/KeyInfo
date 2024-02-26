package com.example.keyinfo.data.network.api

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.domain.model.RefreshToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApiService {
    @POST("api/v1/auth/token")
    suspend fun getNewToken(@Body refresh: RefreshToken): Response<TokenResponse>

    @POST("api/v1/auth/refresh")
    suspend fun getNewRefreshToken(@Body refresh: RefreshToken): Response<TokenResponse>
}