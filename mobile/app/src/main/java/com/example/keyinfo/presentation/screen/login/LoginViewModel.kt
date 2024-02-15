package com.example.keyinfo.presentation.screen.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.R
import com.example.keyinfo.common.Constants
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.data.storage.LocalStorage
import com.example.keyinfo.domain.model.authorization.Login
import com.example.keyinfo.domain.state.LoginState
import com.example.keyinfo.domain.usecase.PostLoginUseCase
import com.example.keyinfo.presentation.navigation.router.AppRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel (
    private val context: Context,
    private val router: AppRouter
) : ViewModel() {
    private val emptyState = LoginState(
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.FALSE,
        Constants.FALSE,
        null,
        Constants.FALSE
    )

    private val _state = MutableStateFlow(emptyState)
    val state: StateFlow<LoginState> get() = _state

    private val postLoginUseCase = PostLoginUseCase()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                performLogin(_state.value.login, _state.value.password) {
                    router.toMain()
                    clearData()
                }
            }
            LoginIntent.GoBack -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                router.toAuth()
            }
            is LoginIntent.UpdateLogin -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                _state.value = state.value.copy(login = intent.login.trim())
            }
            is LoginIntent.UpdatePassword -> {
                processIntent(LoginIntent.UpdateErrorText(null))
                _state.value = state.value.copy(password = intent.password.trim())
            }
            is LoginIntent.UpdatePasswordVisibility -> {
                _state.value = state.value.copy(isPasswordHide = !_state.value.isPasswordHide)
            }
            LoginIntent.UpdateError -> {
                _state.value = state.value.copy(isError = !_state.value.isError)
            }
            is LoginIntent.UpdateErrorText -> {
                _state.value = state.value.copy(isErrorText = intent.errorText)
            }
            LoginIntent.UpdateLoading -> {
                _state.value = state.value.copy(isLoading = !_state.value.isLoading)
            }
            LoginIntent.GoToRegistration -> {
                router.toRegistration()
            }
        }
    }

    fun isLoginButtonAvailable() : Boolean {
        return  state.value.password.isNotEmpty() &&
                state.value.login.isNotEmpty()
    }

    private fun clearData() {
        processIntent(LoginIntent.UpdateLogin(Constants.EMPTY_STRING))
        processIntent(LoginIntent.UpdatePassword(Constants.EMPTY_STRING))
    }

    private fun performLogin(username: String, password: String, routeAfterLogin: () -> Unit) {
        val login = Login(username, password)
        processIntent(LoginIntent.UpdateLoading)
        viewModelScope.launch {
            try {
                val result = postLoginUseCase.invoke(login)
                if (result.isSuccess) {
                    val tokenResponse = result.getOrNull()
                    if (tokenResponse != null) {
                        NetworkService.setAuthToken(tokenResponse.accessToken)
                    }
                    LocalStorage(context).saveToken(tokenResponse!!)
                    routeAfterLogin()
                } else {
                    processIntent(LoginIntent.UpdateErrorText(context.getString(R.string.auth_error)))
                }
            } catch (e: Exception) {
                Log.d("SsS", e.toString())
                Toast.makeText(
                    context,
                    "Ошибка соединения с сервером",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                processIntent(LoginIntent.UpdateLoading)
            }
        }
    }
}