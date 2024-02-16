package com.example.keyinfo.domain.state

data class KeysState(
    val currentButton: Int = 0,
    val isConfirmDialogOpen: Boolean = false,
    val isTransferDialogOpen: Boolean = false
)
