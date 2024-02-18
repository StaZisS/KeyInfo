package com.example.keyinfo.domain.model.request


data class Request (
    val application_id: String,
    val owner_id: String,
    val owner_name: String,
    val start_time: String,
    val end_time: String,
    val is_duplicate: Boolean,
    val end_time_duplicate: String,
    val build_id: Int,
    val room_id: Int,
    val status: String
)