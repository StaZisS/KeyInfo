package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.ScheduleRepository
import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.domain.model.schedule.AudienceRequest
import retrofit2.HttpException

class GetAudienceUseCase {
    private val scheduleRepository = ScheduleRepository()

    suspend fun invoke(request: AudienceRequest): Result<List<Audience>> {
        return try {
            val response = scheduleRepository.getAudience(request)
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