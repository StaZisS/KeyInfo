package com.example.keyinfo.domain.usecase

import android.util.Log
import com.example.keyinfo.data.repository.ScheduleRepository
import com.example.keyinfo.domain.model.schedule.ReserveAudienceRequest
import retrofit2.HttpException

class ReserveAudienceUseCase {
    private val scheduleRepository = ScheduleRepository()

    suspend fun invoke(request: ReserveAudienceRequest): Result<Unit> {
        return try {
            val response = scheduleRepository.reserveAudience(request)
            Log.d("ReserveAudienceUseCase", "invoke: ${response.body()}")
            Log.d("ReserveAudienceUseCase", "invoke: ${response.message()}")
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