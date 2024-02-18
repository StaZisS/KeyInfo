package com.example.keyinfo.data.network.api

import com.example.keyinfo.domain.model.keys.Key
import retrofit2.Response
import retrofit2.http.GET

interface KeysApiService {
    @GET("/api/v1/keys")
    suspend fun getUserKeys(): Response<ArrayList<Key>>
}