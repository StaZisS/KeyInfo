package com.example.keyinfo.presentation.screen.schedule

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keyinfo.common.Constants.CLASSES
import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.domain.model.schedule.AudienceRequest
import com.example.keyinfo.domain.model.schedule.ReserveAudienceRequest
import com.example.keyinfo.domain.usecase.GetAudienceUseCase
import com.example.keyinfo.domain.usecase.GetBuildingsUseCase
import com.example.keyinfo.domain.usecase.ReserveAudienceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

class ScheduleViewModel(val context: Context) : ViewModel() {
    var buildings: List<Int> = mutableListOf()
    var audiences: List<Audience> = mutableListOf()

    var untilDate = mutableStateOf("")
    var checkBoxChecked = mutableStateOf(false)
    var confirmDialogOpened = mutableStateOf(false)
    var searchText = mutableStateOf("")
    var selectedBuilding = mutableStateOf<Int?>(null)
    var selectedDate = mutableStateOf<LocalDate?>(null)
    var selectedTimeIndex = mutableStateOf<Int?>(null)
    var dialogVisible = mutableStateOf(false)
    var currentAudience = mutableStateOf<Audience?>(null)

    var isLoading = mutableStateOf(false)


    private val getBuildingsUseCase = GetBuildingsUseCase()
    private val getAudienceUseCase = GetAudienceUseCase()
    private val reserveAudienceUseCase = ReserveAudienceUseCase()


    fun getBuildings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getBuildingsUseCase.invoke()
                withContext(Dispatchers.Main) {
                    result.fold(onSuccess = {
                        buildings = result.getOrNull()!!
                    }, onFailure = { exception ->
                        handleRequestException(exception)
                    })
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast(
                        "Превышено время ожидания соединения. " + "Пожалуйста, проверьте ваше интернет-соединение."
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ScheduleViewModel", "getBuildings: ${e.message}")
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    fun getAudience() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val requestData = AudienceRequest(
                startTime = OffsetDateTime.of(
                    selectedDate.value,
                    CLASSES[selectedTimeIndex.value!!].startTime,
                    ZoneOffset.UTC
                ),
                endTime = OffsetDateTime.of(
                    selectedDate.value,
                    CLASSES[selectedTimeIndex.value!!].endTime,
                    ZoneOffset.UTC
                ),
                building = selectedBuilding.value!!
            )
            Log.d("ScheduleViewModel", "getAudience req: $requestData")
            try {
                val result = getAudienceUseCase.invoke(requestData)
                Log.d("ScheduleViewModel", "getAudience result: $result")
                withContext(Dispatchers.Main) {
                    result.fold(onSuccess = {
                        audiences = result.getOrNull()!!
                        Log.d("ScheduleViewModel", "getAudience success: $audiences")
                        isLoading.value = false
                    }, onFailure = { exception ->
                        handleRequestException(exception)
                    })
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast(
                        "Превышено время ожидания соединения. " + "Пожалуйста, проверьте ваше интернет-соединение."
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ScheduleViewModel", "getAudience: ${e.message}")
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }

    private fun formatDateToISO8601(birthDate: String): OffsetDateTime {
        val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

        val date: Date = inputFormat.parse(birthDate)!!
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formattedDate: String = outputFormat.format(date)
        return OffsetDateTime.parse(formattedDate)
    }


    fun reserveAudience() {
        viewModelScope.launch(Dispatchers.IO) {
            val requestData = ReserveAudienceRequest(
                building = selectedBuilding.value!!,
                audience = currentAudience.value!!.audience,
                startTime = currentAudience.value!!.startTime,
                endTime = currentAudience.value!!.endTime,
                role = "TEACHER",
                isDuplicate = checkBoxChecked.value,
                untilWhenDuplicate = if (checkBoxChecked.value) {
                    formatDateToISO8601(untilDate.value)
                } else {
                    null
                }
            )
            Log.d("ScheduleViewModel", "reserveAudience: $requestData")
            try {
                val result = reserveAudienceUseCase.invoke(requestData)
                withContext(Dispatchers.Main) {
                    result.fold(onSuccess = {
                        showToast("Аудитория успешно забронирована")
                    }, onFailure = { exception ->
                        handleRequestException(exception)
                    })
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    showToast(
                        "Превышено время ожидания соединения. " + "Пожалуйста, проверьте ваше интернет-соединение."
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d("ScheduleViewModel", "getBuildings: ${e.message}")
                    showToast("Произошла ошибка: ${e.message}")
                }
            }
        }
    }


    private fun handleRequestException(exception: Throwable) {
        when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    400 -> showToast("Не удалось создать дублирующуюся заявку")
                    401 -> showToast("Ошибка авторизации")
                    403 -> showToast("Доступ запрещен")
                    404 -> showToast("Ресурс не найден")
                    500 -> showToast("Внутренняя ошибка сервера")
                    else -> {
                        Log.d("ScheduleViewModel", "handleRequestException: ${exception.message}")
                        showToast("Неизвестная ошибка")
                    }
                }
            }

            else -> {
                showToast("Произошла ошибка: ${exception.message}")
                Log.d("ScheduleViewModel", "handleRequestException: ${exception.message}")

            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}