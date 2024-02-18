package com.example.keyinfo.data.network.api

import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.domain.model.schedule.ReserveAudienceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.OffsetDateTime

interface ScheduleApiService {
    @GET("api/v1/audiences/accommodations/buildings")
    suspend fun getBuildings(): Response<List<Int>>

    @GET("/api/v1/schedules/audience")
    suspend fun getAudience(
        @Query("start_time") startTime: OffsetDateTime,
        @Query("end_time") endTime: OffsetDateTime,
        @Query("build_id") building: Int,
        @Query("room_id") audience: Int? = null
    ): Response<List<Audience>>

    @POST("/api/v1/requests")
    suspend fun reserveAudience(
        @Body request: ReserveAudienceRequest
    ): Response<Unit>
}