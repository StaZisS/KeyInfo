package com.example.keyinfo.data.network.api

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.domain.model.RefreshToken
import com.example.keyinfo.domain.model.authorization.Login
import com.example.keyinfo.domain.model.authorization.Registration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApiService {
    @POST("api/v1/auth/register")
    suspend fun postRegistration(@Body registration: Registration): Response<TokenResponse>

    @POST("api/v1/auth/login")
    suspend fun postLogin(@Body login: Login): Response<TokenResponse>

    @POST("api/v1/auth/logout")
    suspend fun postLogout(@Body refresh: RefreshToken): Response<Unit>
}