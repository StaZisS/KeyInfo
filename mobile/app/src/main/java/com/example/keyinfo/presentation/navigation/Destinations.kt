package com.example.keyinfo.presentation.navigation

import com.example.keyinfo.R


sealed class Screen(val route: String, val title: String, val imageResource: Int? = null) {
    data object Splash : Screen("splash", "Splash", null)
    data object Welcome : Screen("welcome", "Welcome", null)
    data object Login : Screen("login", "Login", null)
    data object Registration : Screen("registration", "Registration", null)
    data object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", null)
    data object Main : Screen("main", "Главная", R.drawable.home)

    data object Schedule : Screen("movie", "Расписание", R.drawable.calendar)

    data object Key : Screen("favorite", "Ключи", null)

    data object Profile : Screen("profile", "Профиль", R.drawable.profile)
}
