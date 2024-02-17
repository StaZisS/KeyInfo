package com.example.keyinfo.data.network.api

import com.example.keyinfo.domain.model.request.Request
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApiService {
    @GET("api/v1/requests")
    suspend fun getRequests(@Query("status") status: String): Response<ArrayList<Request>>

    @DELETE("api/v1/requests")
    suspend fun deleteRequest(@Query("id") id: String): Response<Unit>
}