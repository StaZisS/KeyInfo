package com.example.keyinfo.domain.validator

import com.example.keyinfo.common.Constants

interface Validator {
    fun validate(data: String, secondData: String = Constants.EMPTY_STRING): Int?
}