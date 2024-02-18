package com.example.keyinfo.domain.state

import com.example.keyinfo.common.Constants
import com.example.keyinfo.domain.model.request.Request

data class MainState (
    var currentRequest: Request? = null,
    var isDialogOpen: Boolean = Constants.FALSE,
    var acceptedRequests: List<Request> = listOf(),
    var processRequests: List<Request> = listOf(),
    var declinedRequests: List<Request> = listOf()
)