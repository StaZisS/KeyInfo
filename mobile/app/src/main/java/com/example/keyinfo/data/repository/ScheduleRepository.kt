package com.example.keyinfo.data.repository

import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.domain.model.schedule.AudienceRequest
import com.example.keyinfo.domain.model.schedule.ReserveAudienceRequest
import retrofit2.Response

class ScheduleRepository {

    suspend fun getBuildings(): Response<List<Int>> {
        return NetworkService.scheduleApiService.getBuildings()
    }

    suspend fun getAudience(request: AudienceRequest): Response<List<Audience>> {
        return NetworkService.scheduleApiService.getAudience(
            request.startTime,
            request.endTime,
            request.building,
            request.audience
        )
    }

    suspend fun reserveAudience(request: ReserveAudienceRequest): Response<Unit> {
        return NetworkService.scheduleApiService.reserveAudience(request)
    }
}