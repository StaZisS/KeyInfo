package com.example.keyinfo.presentation.navigation.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val icon_focused: ImageVector
) {

    data object Home: BottomBarScreen(
        route = "main",
        title = "Главная",
        icon = Icons.Outlined.Home,
        icon_focused = Icons.Filled.Home
    )

    data object Timetable: BottomBarScreen(
        route = "timetable",
        title = "Расписание",
        icon = Icons.Outlined.CalendarMonth,
        icon_focused = Icons.Filled.CalendarMonth
    )

    data object Profile: BottomBarScreen(
        route = "profile",
        title = "Профиль",
        icon = Icons.Outlined.Person,
        icon_focused = Icons.Filled.Person
    )

}