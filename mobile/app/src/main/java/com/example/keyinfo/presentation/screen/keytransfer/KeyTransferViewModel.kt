package com.example.keyinfo.presentation.screen.keytransfer

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.domain.model.keys.KeyId
import com.example.keyinfo.domain.model.keys.User
import com.example.keyinfo.domain.model.request.TransferStatus
import com.example.keyinfo.domain.state.KeysState
import com.example.keyinfo.domain.usecase.AcceptRequestUseCase
import com.example.keyinfo.domain.usecase.CreateTransferUseCase
import com.example.keyinfo.domain.usecase.DeclineRequestUseCase
import com.example.keyinfo.domain.usecase.DeleteMyTransferUseCase
import com.example.keyinfo.domain.usecase.GetAllUsersUseCase
import com.example.keyinfo.domain.usecase.GetForeignRequestsUseCase
import com.example.keyinfo.domain.usecase.GetMyRequestsUseCase
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
    private val acceptRequestUseCase = AcceptRequestUseCase()
    private val declineRequestUseCase = DeclineRequestUseCase()
    private val deleteMyTransferUseCase = DeleteMyTransferUseCase()
    private val getForeignRequestsUseCase = GetForeignRequestsUseCase()
    private val getMyRequestsUseCase = GetMyRequestsUseCase()
    private val getAllUsersUseCase = GetAllUsersUseCase()
    private val createTransferUseCase = CreateTransferUseCase()

    var myKeysTransferDialogOpen = mutableStateOf(false)
    var transferDialogOpened = mutableStateOf(false)
    val selectedUser = mutableStateOf<User?>(null)

    fun processIntent(intent: KeyTransferIntent) {
        when (intent) {
            KeyTransferIntent.AcceptTransfer -> {

            }

            KeyTransferIntent.ConfirmReserve -> {

            }

            KeyTransferIntent.RejectTransfer -> {

            }

            is KeyTransferIntent.ClickOnCard -> {
                _state.value.currentTransfer = intent.transfer
                transferDialogOpened.value = !transferDialogOpened.value
            }

            is KeyTransferIntent.ClickOnButton -> {
                _state.value = state.value.copy(currentButton = intent.id)
                if (intent.id == 0) {
                    getForeignRequests(TransferStatus.IN_PROCESS)
                } else {
                    getMyRequests(TransferStatus.IN_PROCESS)
                }
            }

            KeyTransferIntent.UpdateConfirmDialogState -> {
                myKeysTransferDialogOpen.value = !myKeysTransferDialogOpen.value
            }
            KeyTransferIntent.UpdateTransferDialogState -> {
                transferDialogOpened.value = !transferDialogOpened.value
            }
        }
    }

    fun updateConfirmDialogState(selectedUser: User?) {
        this.selectedUser.value = selectedUser
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

    fun getMyRequests(status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getMyRequestsUseCase.invoke(status)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { requests ->
                            Log.d("MY_REQUESTS", requests.toString())
                            _state.value.myRequests = result.getOrNull()!!
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun getForeignRequests(status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getForeignRequestsUseCase.invoke(status)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { requests ->
                            Log.d("FOREIGN_REQUESTS", requests.toString())
                            _state.value.fromMeRequests = result.getOrNull()!!
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun deleteMyTransfer(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = deleteMyTransferUseCase.invoke(id)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            Log.d("DELETE_TRANSFER", "Transfer request with ID $id was successfully deleted.")
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            } finally {
                getMyRequests(TransferStatus.IN_PROCESS)
                getForeignRequests(TransferStatus.IN_PROCESS)
            }
        }
    }

    fun acceptRequest(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = acceptRequestUseCase.invoke(id)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            Log.d("DELETE_TRANSFER", "Transfer request with ID $id was successfully deleted.")
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            } finally {
                getMyRequests(TransferStatus.IN_PROCESS)
                getForeignRequests(TransferStatus.IN_PROCESS)
            }
        }
    }

    fun declineRequest(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = declineRequestUseCase.invoke(id)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            Log.d("DELETE_TRANSFER", "Transfer request with ID $id was successfully deleted.")
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            } finally {
                getMyRequests(TransferStatus.IN_PROCESS)
                getForeignRequests(TransferStatus.IN_PROCESS)
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getAllUsersUseCase.invoke()
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { users ->
                            Log.d("USERS", users.toString())
                            _state.value.users = result.getOrNull()!!
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun createTransfer(user: User?, keyId: String) {
        if (user == null) {
            showToast("Необходимо выбрать пользователя")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = createTransferUseCase.invoke(user.clientId, keyId)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = {
                            Log.d("TRANSFER", "Успешная передача ключа")
                        },
                        onFailure = { exception ->
                            handleRegistrationError(exception)
                        }
                    )
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast("Превышено время ожидания соединения. Пожалуйста, проверьте ваше интернет-соединение.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Произошла ошибка: ${e.message}")
                }
            } finally {
                getUserKeys()
                selectedUser.value = null
                processIntent(KeyTransferIntent.UpdateConfirmDialogState)
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