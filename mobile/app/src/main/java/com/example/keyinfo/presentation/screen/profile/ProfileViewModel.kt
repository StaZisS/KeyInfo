package com.example.keyinfo.presentation.screen.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.common.Constants
import com.example.keyinfo.common.Formatter
import com.example.keyinfo.data.model.TokenResponse
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.data.storage.LocalStorage
import com.example.keyinfo.domain.model.RefreshToken
import com.example.keyinfo.domain.state.ProfileState
import com.example.keyinfo.domain.usecase.DeleteTokenUseCase
import com.example.keyinfo.domain.usecase.GetProfileUseCase
import com.example.keyinfo.domain.usecase.PostLogoutUseCase
import com.example.keyinfo.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ProfileViewModel(val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> get() = _state

    private val getProfileUseCase = GetProfileUseCase()
    private val postLogoutUseCase = PostLogoutUseCase()
    private val refreshTokenUseCase = RefreshTokenUseCase()
    private val deleteTokenUseCase = DeleteTokenUseCase(context)

    fun logoutUser(toAfterLogout: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = postLogoutUseCase.invoke(LocalStorage(context).getToken().refreshToken)
                if (result.isSuccess) {
                    deleteTokenUseCase.invoke()
                    NetworkService.setAuthToken(Constants.EMPTY_STRING)
                    withContext(Dispatchers.Main) {
                        toAfterLogout()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = {},
                            onFailure = { exception ->
                                handleRegistrationError(exception)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("eee", e.message.toString())
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun refreshToken() {
        val token = LocalStorage(context).getToken().refreshToken
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = refreshTokenUseCase.invoke(RefreshToken(token))
                if (result.isSuccess) {
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = {
                                LocalStorage(context).saveToken(
                                    TokenResponse(
                                        it.accessToken,
                                        token
                                    )
                                )
                                NetworkService.setAuthToken(it.accessToken)
                            },
                            onFailure = { exception ->
                                handleRegistrationError(exception)
                            }
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = {},
                            onFailure = { exception ->
                                handleRegistrationError(exception)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("eee", e.message.toString())
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun getProfile() {
        _state.value.isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getProfileUseCase.invoke()
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            _state.value.name = result.getOrNull()!!.name;
                            if (_state.value.name == "UNSPECIFIED") {
                                refreshToken()
                            }
                            _state.value.role =
                                Formatter.getNormalRoleString(result.getOrNull()!!.roles)
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast(
                        "Превышено время ожидания соединения. " +
                                "Пожалуйста, проверьте ваше интернет-соединение."
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            } finally {
                _state.value.isLoading = false

            }
        }
    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> showToast("Something went wrong...")
                else -> showToast("Неизвестная ошибка: ${exception.code()}")
            }

            else -> showToast("Ошибка соединения с сервером")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}