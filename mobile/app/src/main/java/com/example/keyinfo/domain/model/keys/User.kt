package com.example.keyinfo.domain.model.keys

data class User(
    val clientId: String,
    val name: String,
    val email: String,
    val gender: String,
    val createdDate: String,
    val roles: ArrayList<String>
)