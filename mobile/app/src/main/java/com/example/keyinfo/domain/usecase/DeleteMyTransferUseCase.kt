package com.example.keyinfo.domain.usecase

import com.example.keyinfo.data.repository.KeysRepository
import retrofit2.HttpException

class DeleteMyTransferUseCase {
    private val keysRepository = KeysRepository()

    suspend fun invoke(id: String): Result<Unit> {
        return try {
            val response = keysRepository.deleteMyTransfer(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}