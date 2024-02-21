package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import com.example.keyinfo.domain.model.keys.KeyId
import retrofit2.HttpException

class CreateTransferUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(receiverId: String, keyId: String): Result<Unit> {
        return try {
            val response = keysRepository.createTransfer(receiverId, KeyId(keyId))
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