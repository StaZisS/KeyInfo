package com.example.keyinfo.domain.state

data class RegistrationState (
    val name: String,
    val gender: Int,
    val email: String,
    val birthday: String,
    val date: String,

    val isDatePickerOpened: Boolean,
    val isSecondScreenAvailable: Boolean,

    val password: String,
    val confirmPassword: String,

    val isPasswordHide: Boolean,
    val isConfirmPasswordHide: Boolean,

    val isError: Boolean,
    val isErrorPasswordText: String?,
    val isErrorNameText: String?,
    val isErrorEmailText: String?,
    val isErrorConfirmPasswordText: String?,
    val isErrorLoginText: String?,

    val isLoading: Boolean
)