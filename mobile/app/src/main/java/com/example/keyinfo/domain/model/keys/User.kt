package com.example.keyinfo.domain.model.keys

import java.time.OffsetDateTime

data class User (
    val clientId: String,
    val name: String,
    val email: String,
    val gender: String,
    val createdDate: OffsetDateTime,
    val roles: List<String>
)