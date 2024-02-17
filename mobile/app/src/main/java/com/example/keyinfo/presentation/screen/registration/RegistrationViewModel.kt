package com.example.keyinfo.presentation.screen.registration

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.common.Constants
import com.example.keyinfo.data.network.NetworkService
import com.example.keyinfo.data.storage.LocalStorage
import com.example.keyinfo.domain.model.authorization.Registration
import com.example.keyinfo.domain.state.RegistrationState
import com.example.keyinfo.domain.usecase.DataValidateUseCase
import com.example.keyinfo.domain.usecase.PostRegistrationUseCase
import com.example.keyinfo.domain.validator.ConfirmPasswordValidator
import com.example.keyinfo.domain.validator.EmailValidator
import com.example.keyinfo.domain.validator.NameValidator
import com.example.keyinfo.domain.validator.PasswordValidator
import com.example.keyinfo.presentation.navigation.router.AppRouter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class RegistrationViewModel (
    private val context: Context,
    private val router: AppRouter
) : ViewModel() {
    private val emptyState = RegistrationState(
        Constants.EMPTY_STRING,
        "MALE",
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.FALSE,
        Constants.FALSE,
        Constants.EMPTY_STRING,
        Constants.EMPTY_STRING,
        Constants.FALSE,
        Constants.FALSE,
        Constants.FALSE,
        null,
        null,
        null,
        null,
        null,
        Constants.FALSE
    )

    private val _state = MutableStateFlow(emptyState)
    val state: StateFlow<RegistrationState> get() = _state

    private val postRegistrationUseCase = PostRegistrationUseCase()
    private val dataValidateUseCase = DataValidateUseCase()

    fun processIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.UpdateBirthday -> {
                _state.value = state.value.copy(birthday = intent.birthday)
                _state.value = state.value.copy(date = intent.date)
            }
            is RegistrationIntent.UpdateDatePickerVisibility -> {
                _state.value = state.value.copy(
                    isDatePickerOpened = !_state.value.isDatePickerOpened
                )
            }
            is RegistrationIntent.UpdateEmail -> {
                _state.value = state.value.copy(email = intent.email.trim())
            }
            is RegistrationIntent.UpdateGender -> {
                _state.value = state.value.copy(gender = intent.gender)
            }
            is RegistrationIntent.UpdateName -> {
                _state.value = state.value.copy(name = intent.name)
            }
            is RegistrationIntent.UpdateConfirmPassword -> {
                _state.value = state.value.copy(confirmPassword = intent.confirmPassword.trim())
            }
            is RegistrationIntent.UpdateConfirmPasswordVisibility -> {
                _state.value = state.value.copy(
                    isConfirmPasswordHide = !_state.value.isConfirmPasswordHide
                )
            }
            is RegistrationIntent.UpdatePassword -> {
                _state.value = state.value.copy(password = intent.password.trim())
            }
            is RegistrationIntent.UpdatePasswordVisibility -> {
                _state.value = state.value.copy(
                    isPasswordHide = !_state.value.isPasswordHide
                )
            }
            is RegistrationIntent.Registration -> {
                performRegistration(state.value) {
                    router.toMain()
                    clearData()
                }
            }
            is RegistrationIntent.UpdateErrorText -> {
                val result = dataValidateUseCase.invoke(intent.validator, intent.data, intent.secondData)
                when (intent.validator) {
                    is EmailValidator -> _state.value = state.value.copy (
                        isErrorEmailText = result?.let { context.getString(it) }
                    )
                    is PasswordValidator -> _state.value = state.value.copy (
                        isErrorPasswordText = result?.let { context.getString(it) }
                    )
                    is ConfirmPasswordValidator -> _state.value = state.value.copy (
                        isErrorConfirmPasswordText = result?.let { context.getString(it) }
                    )
                    is NameValidator -> _state.value = state.value.copy(
                        isErrorNameText = result?.let { context.getString(it) }
                    )
                }
            }
            RegistrationIntent.UpdateLoading -> {
                _state.value = state.value.copy(
                    isLoading = !_state.value.isLoading
                )
            }

            RegistrationIntent.GoToSecondScreen -> {
                router.toPasswordRegistration()
            }

            RegistrationIntent.GoBackToAuth -> {
                router.toAuth()
            }

            RegistrationIntent.GoBackToFirst -> {
                router.toRegistration()
            }

            RegistrationIntent.GoToLogin -> {
                router.toLogin()
            }
        }
    }

    fun isDatePickerOpen() : Boolean {
        return state.value.isDatePickerOpened
    }

    fun isContinueButtonAvailable() : Boolean {
        return  state.value.name.isNotEmpty() &&
                state.value.email.isNotEmpty() &&
                state.value.date.isNotEmpty() &&
                state.value.isErrorEmailText == null &&
                state.value.isErrorNameText == null
    }

    fun isRegisterButtonAvailable() : Boolean {
        return  state.value.password.isNotEmpty() &&
                state.value.confirmPassword.isNotEmpty() &&
                state.value.isErrorPasswordText == null &&
                state.value.isErrorConfirmPasswordText == null
    }

    private fun clearData() {
        processIntent(RegistrationIntent.UpdateName(Constants.EMPTY_STRING))
        processIntent(RegistrationIntent.UpdatePassword(Constants.EMPTY_STRING))
        processIntent(RegistrationIntent.UpdateConfirmPassword(Constants.EMPTY_STRING))
        processIntent(
            RegistrationIntent.UpdateBirthday(
            Constants.EMPTY_STRING, Constants.EMPTY_STRING)
        )
        processIntent(RegistrationIntent.UpdateGender(Constants.EMPTY_STRING))
    }

    private fun performRegistration(registrationState: RegistrationState, afterRegistration: () -> Unit) {
        val registration = Registration(
            name = registrationState.name.trim(),
            password = registrationState.password.trim(),
            email = registrationState.email.trim(),
            gender = "MALE"
        )

        processIntent(RegistrationIntent.UpdateLoading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = postRegistrationUseCase.invoke(registration)
                withContext(Dispatchers.Main) {
                    result.fold(
                        onSuccess = { tokenResponse ->
                            LocalStorage(context).saveToken(tokenResponse)
                            NetworkService.setAuthToken(tokenResponse.accessToken)
                            afterRegistration()
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
                processIntent(RegistrationIntent.UpdateLoading)
            }
        }

    }

    private fun handleRegistrationError(exception: Throwable) {
        when (exception) {
            is HttpException -> when (exception.code()) {
                400 -> showToast("Ошибка регистрации")
                else -> showToast("Неизвестная ошибка: ${exception.code()}")
            }
            else -> showToast("Ошибка соединения с сервером")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}