package com.example.keyinfo.data.repository

import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.domain.model.keys.Key
import retrofit2.Response

class KeysRepository {
    suspend fun getUserKeys(): Response<ArrayList<Key>>{
        return NetworkService.keysApiService.getUserKeys()
    }
}