package com.example.keyinfo.data.repository

import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.keys.Key
import com.example.keyinfo.domain.model.keys.Transfer
import retrofit2.Response

class KeysRepository {
    private val keysApiService = NetworkService.keysApiService

    suspend fun getUserKeys(): Response<ArrayList<Key>> {
        return keysApiService.getUserKeys()
    }

    suspend fun getMyRequests(status: String): Response<ArrayList<Transfer>> {
        return keysApiService.getMyRequests(status)
    }

    suspend fun getForeignRequests(status: String): Response<ArrayList<Transfer>> {
        return keysApiService.getForeignRequests(status)
    }

    suspend fun deleteMyTransfer(id: String): Response<Unit> {
        return keysApiService.deleteMyTransfer(id)
    }

    suspend fun acceptRequest(id: String): Response<Unit> {
        return keysApiService.acceptRequest(id)
    }

    suspend fun declineRequest(id: String): Response<Unit> {
        return keysApiService.declineRequest(id)
    }
}
