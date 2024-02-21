package com.example.keyinfo.domain.usecase

import com.example.keyinfo.common.Constants
import com.example.keyinfo.domain.validator.Validator

class DataValidateUseCase {
    fun invoke(
        validator: Validator,
        data: String,
        secondData: String = Constants.EMPTY_STRING
    ): Int? {
        return validator.validate(data, secondData)
    }
}