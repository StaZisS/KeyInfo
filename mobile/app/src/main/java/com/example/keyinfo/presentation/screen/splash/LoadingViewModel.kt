package com.example.keyinfo.presentation.screen.splash


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.data.storage.LocalStorage
import com.example.keyinfo.domain.model.ApiResult
import com.example.keyinfo.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoadingViewModel(private val context: Context) : ViewModel() {
    private val getProfileUseCase = GetProfileUseCase()
    fun checkUser(onResult: (ApiResult<Nothing>) -> Unit) {
        viewModelScope.launch {
            if (LocalStorage(context).hasToken()) {
                NetworkService.setAuthToken(LocalStorage(context).getToken().accessToken)
                try {
                    val result = getProfileUseCase.invoke()
                    withContext(Dispatchers.Main) {
                        result.fold(
                            onSuccess = {
                                onResult(ApiResult.Success())
                            },
                            onFailure = { exception ->
                                handleRegistrationError(exception)
                                onResult(ApiResult.Error())
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
                    onResult(ApiResult.Error())
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        showToast("Произошла ошибка: ${e.message}")
                    }
                    onResult(ApiResult.Error())
                }
            } else {
                onResult(ApiResult.Error())
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
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}

