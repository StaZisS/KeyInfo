package com.example.keyinfo.presentation.navigation

import com.example.keyinfo.R


sealed class Screen(
    val route: String,
    val title: String,
    val imageResource: Int? = null,
    val imageResourceFilled: Int? = null
) {
    data object Splash : Screen("splash", "Splash", null)
    data object Welcome : Screen("welcome", "Welcome", null)
    data object Login : Screen("login", "Login", null)
    data object Registration : Screen("registration", "Registration", null)
    data object RegistrationPwd : Screen("registrationPwd", "RegistrationPwd", null)
    data object Main : Screen("main", "Главная", R.drawable.home, R.drawable.home_filled)

    data object Schedule :
        Screen("movie", "Расписание", R.drawable.bottom_calendar, R.drawable.calendar_filled)

    data object Key : Screen("favorite", "Ключи", R.drawable.key, R.drawable.key_filled)

    data object Profile :
        Screen("profile", "Профиль", R.drawable.profile, R.drawable.profile_filled)
}
