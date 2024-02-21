package com.example.keyinfo.domain.model.keys

data class Key(
    val key_id: String,
    val last_access: String,
    val build: Int,
    val room: Int
)
