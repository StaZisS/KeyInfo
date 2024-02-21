package com.example.keyinfo.domain.validator

import com.example.keyinfo.R

class ConfirmPasswordValidator : Validator {
    override fun validate(data: String, secondData: String): Int? {
        return when {
            data != secondData -> R.string.password_result3
            else -> null
        }
    }
}