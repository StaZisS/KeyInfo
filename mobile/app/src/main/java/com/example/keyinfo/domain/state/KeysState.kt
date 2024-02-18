package com.example.keyinfo.domain.state

import com.example.keyinfo.domain.model.keys.Key

data class KeysState(
    val currentButton: Int = 0,
    val isConfirmDialogOpen: Boolean = false,
    val isTransferDialogOpen: Boolean = false,
    var myKeys: ArrayList<Key> = arrayListOf()
)
