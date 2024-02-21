package com.example.keyinfo.presentation.screen.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keyinfo.presentation.navigation.router.AppRouter

class RegistrationViewModelFactory(
    private val context: Context,
    private val router: AppRouter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(context, router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
