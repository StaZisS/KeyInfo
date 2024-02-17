package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.ProfileRepository
import com.example.keyinfo.domain.model.authorization.Login
import com.example.keyinfo.domain.model.profile.Profile
import retrofit2.HttpException

class GetProfileUseCase {
    private val profileRepository = ProfileRepository()

    suspend fun invoke(): Result<Profile> {
        return try {
            val response = profileRepository.getProfile()
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