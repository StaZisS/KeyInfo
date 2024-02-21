package com.example.keyinfo.data.repository

import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.profile.Profile
import retrofit2.Response

class ProfileRepository {
    suspend fun getProfile(): Response<Profile> {
        return NetworkService.profileApiService.getProfile()
    }
}