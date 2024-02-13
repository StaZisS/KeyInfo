package com.example.keyinfo.presentation.screen.registration

import com.example.keyinfo.common.Constants
import com.example.keyinfo.domain.state.RegistrationState
import com.example.keyinfo.domain.validator.Validator

sealed class RegistrationIntent {
    data class UpdateName(val name: String) : RegistrationIntent()
    data class UpdateGender(val gender: Int) : RegistrationIntent()
    data class UpdateEmail(val email: String) : RegistrationIntent()
    data class UpdateBirthday(val birthday: String, val date: String) : RegistrationIntent()
    data object UpdateDatePickerVisibility : RegistrationIntent()

    data class UpdatePassword(val password: String) : RegistrationIntent()
    data class UpdateConfirmPassword(val confirmPassword: String) : RegistrationIntent()
    data object UpdatePasswordVisibility : RegistrationIntent()
    data object UpdateConfirmPasswordVisibility : RegistrationIntent()
    data class Registration(
        val registrationState: RegistrationState
    ): RegistrationIntent()

    data class UpdateErrorText(
        val validator: Validator,
        val data: String,
        val secondData: String = Constants.EMPTY_STRING
    ): RegistrationIntent()

    data object UpdateLoading: RegistrationIntent()
    data object GoToSecondScreen: RegistrationIntent()
    data object GoBackToAuth: RegistrationIntent()
    data object GoBackToFirst: RegistrationIntent()
    data object GoToLogin: RegistrationIntent()
}