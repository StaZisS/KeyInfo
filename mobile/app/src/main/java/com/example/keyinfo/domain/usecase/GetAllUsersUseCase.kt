package com.example.keyinfo.domain.usecase

import android.util.Log
import com.example.keyinfo.data.repository.KeysRepository
import com.example.keyinfo.domain.model.keys.User
import retrofit2.HttpException

class GetAllUsersUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(): Result<ArrayList<User>> {
        return try {
            val response = keysRepository.getAllUsers()
            Log.d("GetAllUsersUseCase", "invoke: ${response.body()}")
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