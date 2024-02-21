package com.example.keyinfo.domain.validator

import com.example.keyinfo.R

class NameValidator : Validator {
    override fun validate(data: String, secondData: String): Int? {
        val pattern = Regex("\\s+")

        return if (data.isEmpty() || data == null) {
            null
        } else if (pattern.matches(data)) {
            R.string.name_error
        } else null
    }

}