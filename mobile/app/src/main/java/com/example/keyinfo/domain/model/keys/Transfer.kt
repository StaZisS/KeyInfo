package com.example.keyinfo.domain.model.keys

data class Transfer(
    val key_id: String,
    val owner_id: String,
    val receiver_id: String,
    val owner_name: String,
    val owner_email: String,
    val receiver_name: String,
    val receiver_email: String,
    val transfer_id: String,
    val build_id: Int,
    val room_id: Int,
    val status: String
)
