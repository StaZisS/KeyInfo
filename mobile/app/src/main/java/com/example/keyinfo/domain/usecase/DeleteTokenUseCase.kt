package com.example.keyinfo.domain.usecase

import android.content.Context
import com.example.keyinfo.common.Constants
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.data.storage.LocalStorage

class DeleteTokenUseCase (private val context: Context) {
    private val localStorage = LocalStorage(context)

    fun invoke() {
        NetworkService.setAuthToken(Constants.EMPTY_STRING)
        return localStorage.removeToken()
    }
}