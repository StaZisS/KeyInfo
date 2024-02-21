package com.example.keyinfo.data.network.api

import com.example.keyinfo.domain.model.profile.Profile
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApiService {
    @GET("api/v1/profiles")
    suspend fun getProfile(): Response<Profile>
}