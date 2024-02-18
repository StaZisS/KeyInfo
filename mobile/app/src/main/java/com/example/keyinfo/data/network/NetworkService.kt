package com.example.keyinfo.data.network

import com.example.keyinfo.data.network.api.AuthenticationApiService
import com.example.keyinfo.data.network.api.ProfileApiService
import com.example.keyinfo.data.network.api.ScheduleApiService
import com.example.keyinfo.data.network.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkService {
    private const val BASE_URL = "http://147.45.76.239:8080/"

    private val authInterceptor = AuthInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun setAuthToken(token: String) {
        authInterceptor.setAuthToken(token)
    }

    val authenticationApiService: AuthenticationApiService =
        retrofit.create(AuthenticationApiService::class.java)

    val profileApiService: ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    val scheduleApiService: ScheduleApiService =
        retrofit.create(ScheduleApiService::class.java)
}