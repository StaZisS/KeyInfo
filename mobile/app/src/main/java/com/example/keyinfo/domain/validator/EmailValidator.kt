package com.example.keyinfo.domain.validator

import com.example.keyinfo.R

class EmailValidator : Validator {
    override fun validate(data: String, secondData: String) : Int?{
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+")

        return if (data.isEmpty() || data == null) {
            null
        } else if (!emailPattern.matches(data)) {
            R.string.email_error
        } else null
    }
}