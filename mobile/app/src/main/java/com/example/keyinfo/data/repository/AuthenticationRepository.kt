package com.example.keyinfo.data.repository

import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.RefreshToken
import com.example.keyinfo.domain.model.authorization.Login
import com.example.keyinfo.domain.model.authorization.Registration
import retrofit2.Response

class AuthenticationRepository {

    suspend fun postLogin(authorizationData: Login): Response<TokenResponse> {
        return NetworkService.authenticationApiService.postLogin(authorizationData)
    }

    suspend fun postRegistration(registration: Registration): Response<TokenResponse> {
        return NetworkService.authenticationApiService.postRegistration(registration)
    }

    suspend fun postLogout(refresh: RefreshToken): Response<Unit> {
        return NetworkService.authenticationApiService.postLogout(refresh)
    }
}