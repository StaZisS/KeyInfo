package com.example.keyinfo.presentation.screen.keytransfer

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.domain.model.request.TransferStatus
import com.example.keyinfo.domain.state.KeysState
import com.example.keyinfo.domain.usecase.GetUserKeysUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class KeyTransferViewModel(private val context: Context) : ViewModel() {

    private val _state = MutableStateFlow(KeysState())
    val state: StateFlow<KeysState> get() = _state

    private val getUserKeysUseCase = GetUserKeysUseCase()

    fun processIntent(intent: KeyTransferIntent) {
        when (intent) {
            KeyTransferIntent.AcceptTransfer -> {

            }

            KeyTransferIntent.ConfirmReserve -> {

            }

            KeyTransferIntent.RejectTransfer -> {

            }

            KeyTransferIntent.UpdateConfirmDialogState -> {
                _state.value =
                    state.value.copy(isConfirmDialogOpen = !state.value.isConfirmDialogOpen)
            }

            KeyTransferIntent.UpdateTransferDialogState -> {
                _state.value =
                    state.value.copy(isTransferDialogOpen = !state.value.isTransferDialogOpen)
            }

            is KeyTransferIntent.ClickOnCard -> {

            }

            is KeyTransferIntent.ClickOnButton -> {
                _state.value = state.value.copy(currentButton = intent.id)
            }
        }
    }

    fun getUserKeys(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getUserKeysUseCase.invoke()
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            Log.d("KEYS", result.getOrNull()!!.toString())
                            _state.value.myKeys = result.getOrNull()!!
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. " +
                            "Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> showToast("Something went wrong...")
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