package com.example.keyinfo.data.repository

import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.request.Request
import retrofit2.Response

class RequestRepository {
    suspend fun getRequests(status: String): Response<ArrayList<Request>>{
        return NetworkService.requestApiService.getRequests(status)
    }

    suspend fun deleteRequest(id: String): Response<Unit>{
        return NetworkService.requestApiService.deleteRequest(id)
    }
}