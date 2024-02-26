package com.example.keyinfo.presentation.screen.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.domain.model.request.TransferStatus
import com.example.keyinfo.domain.state.MainState
import com.example.keyinfo.domain.usecase.DeleteRequestUseCase
import com.example.keyinfo.domain.usecase.GetRequestsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainViewModel(private val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> get() = _state


    private val getRequestsUseCase = GetRequestsUseCase()
    private val deleteRequestUseCase = DeleteRequestUseCase()

    var confirmDialogOpened = mutableStateOf(false)

    fun processIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.ChangeDeleteDialogState -> {
                confirmDialogOpened.value = !confirmDialogOpened.value
            }

            is MainIntent.SetNewRequest -> {
                _state.value.currentRequest = intent.request
            }
        }
    }

    fun deleteRequest(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = deleteRequestUseCase.invoke(id)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            getAcceptedRequests()
                            getProgressRequests()
                            getDeclinedRequests()
                            showToast("Успешно удалено")
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
                processIntent(MainIntent.ChangeDeleteDialogState)
            }
        }
    }

    fun getAcceptedRequests() {
        _state.value.isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getRequestsUseCase.invoke(TransferStatus.ACCEPTED)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { request ->
                            Log.d("Sss", request.toString())
                            _state.value = state.value.copy(acceptedRequests = request)
                        },
                        onFailure = { exception ->
                            Log.d("Sss exception", exception.message.toString())
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

    fun getProgressRequests() {
        _state.value.isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getRequestsUseCase.invoke(TransferStatus.IN_PROCESS)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { request ->
                            _state.value = state.value.copy(processRequests = request)
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

    fun getDeclinedRequests() {
        _state.value.isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getRequestsUseCase.invoke(TransferStatus.DECLINED)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { request ->
                            _state.value = state.value.copy(declinedRequests = request)
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

    fun resetState() {
        _state.value = state.value.copy(declinedRequests = listOf())
        _state.value = state.value.copy(acceptedRequests = listOf())
        _state.value = state.value.copy(processRequests = listOf())
        _state.value = state.value.copy(currentRequest = null)
    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> showToast("Something went wrong...")
                401 -> {}
                else -> showToast("Неизвестная ошибка: ${exception.code()}")
            }

            else -> {
                Log.d("Sss", exception.message.toString())
                showToast("Ошибка соединения с сервером")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}