package com.example.keyinfo.domain.state

import com.example.keyinfo.domain.model.keys.Key
import com.example.keyinfo.domain.model.keys.Transfer
import com.example.keyinfo.domain.model.keys.User

data class KeysState(
    val currentButton: Int = 0,
    var myKeys: ArrayList<Key> = arrayListOf(),
    var myRequests: ArrayList<Transfer> = arrayListOf(),
    var fromMeRequests: ArrayList<Transfer> = arrayListOf(),
    var users: ArrayList<User> = arrayListOf(),
    var currentKey: Key? = null,
    var currentTransfer: Transfer? = null,

    var isLoading: Boolean = true,
)
