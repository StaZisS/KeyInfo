package com.example.keyinfo.domain.state


data class ProfileState(
    var name: String = "Steve Jobs",
    var role: String = "Software Developer",

    var isLoading: Boolean = false,
    var isShaking: Boolean = false,
)