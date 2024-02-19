package com.example.keyinfo.domain.state

import com.example.keyinfo.domain.model.keys.Key
import com.example.keyinfo.domain.model.keys.Transfer

data class KeysState(
    val currentButton: Int = 0,
    var myKeys: ArrayList<Key> = arrayListOf(),
    var myRequests: ArrayList<Transfer> = arrayListOf(),
    var fromMeRequests: ArrayList<Transfer> = arrayListOf()
)
