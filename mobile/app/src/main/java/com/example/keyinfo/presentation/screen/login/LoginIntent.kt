package com.example.keyinfo.presentation.screen.login

sealed class LoginIntent {
    data object Login : LoginIntent()
    data object GoBack: LoginIntent()
    data object GoToRegistration: LoginIntent()
    data class UpdateLogin(val login: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    data object UpdatePasswordVisibility : LoginIntent()

    data object UpdateError: LoginIntent()
    data class UpdateErrorText(val errorText: String?) : LoginIntent()

    data object UpdateLoading: LoginIntent()
}