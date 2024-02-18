package com.example.keyinfo.presentation.screen.main

import com.example.keyinfo.domain.model.request.Request

sealed class MainIntent {
    data object ChangeDeleteDialogState: MainIntent()
    data class SetNewRequest(val request: Request): MainIntent()
}