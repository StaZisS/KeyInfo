package com.example.keyinfo.domain.model

sealed class ApiResult<out T> {
    data class Success<T>(val data: T? = null) : ApiResult<T>()
    data class Error(val message: String? = null, val code: Int? = null) : ApiResult<Nothing>()
}
