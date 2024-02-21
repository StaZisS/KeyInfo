package com.example.keyinfo.presentation.screen.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keyinfo.presentation.navigation.router.AppRouter

class LoginViewModelFactory(
    private val context: Context,
    private val router: AppRouter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context, router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
