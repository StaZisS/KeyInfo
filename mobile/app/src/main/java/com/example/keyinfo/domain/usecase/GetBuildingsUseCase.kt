package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.ScheduleRepository
import retrofit2.HttpException

class GetBuildingsUseCase {
    private val scheduleRepository = ScheduleRepository()

    suspend fun invoke(): Result<List<Int>> {
        return try {
            val response = scheduleRepository.getBuildings()
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

