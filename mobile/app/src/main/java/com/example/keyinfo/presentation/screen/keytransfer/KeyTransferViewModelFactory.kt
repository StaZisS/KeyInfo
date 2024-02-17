package com.example.keyinfo.presentation.screen.keytransfer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class KeyTransferViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeyTransferViewModel::class.java)) {
            return KeyTransferViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
