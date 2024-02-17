package com.example.keyinfo.domain.model.profile

import java.util.Date

data class Profile(
    val clientId: String,
    val name: String,
    val email: String,
    val gender: String,
    val createdDate: Date,
    val roles: ArrayList<String>
)
