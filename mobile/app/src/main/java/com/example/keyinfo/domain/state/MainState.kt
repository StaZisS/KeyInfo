package com.example.keyinfo.domain.state

import com.example.keyinfo.domain.model.request.Request

data class MainState (
    var currentRequest: Request? = null,
    var acceptedRequests: List<Request> = listOf(),
    var processRequests: List<Request> = listOf(),
    var declinedRequests: List<Request> = listOf(),

    var isLoading: Boolean = true
)