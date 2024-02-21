package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import com.example.keyinfo.domain.model.keys.Key
import retrofit2.HttpException

class GetUserKeysUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(): Result<ArrayList<Key>> {
        return try {
            val response = keysRepository.getUserKeys()
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