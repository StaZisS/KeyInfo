package com.example.keyinfo.data.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null

        authToken?.let {
            val authHeader = "Bearer $it"
            request = request.newBuilder()
                .addHeader("Authorization", authHeader)
                .build()
        }

        return try {
            response = chain.proceed(request)
            Log.d("Interceptor", response.message)
            response
        } catch (e: Exception) {
            response?.close()
            chain.proceed(request)
        }
    }
}
