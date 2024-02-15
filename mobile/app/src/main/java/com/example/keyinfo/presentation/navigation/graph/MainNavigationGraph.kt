package com.example.keyinfo.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.keyinfo.presentation.screen.main.MainScreen
import com.example.keyinfo.presentation.screen.profile.ProfileScreen

const val MAIN_ROUTE = "main_root"

fun NavGraphBuilder.mainNavigationGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "main",
        route = MAIN_ROUTE
    ) {
        composable("main"){
            MainScreen()
        }
        composable("profile"){
            ProfileScreen()
        }
        composable("timetable"){
            ProfileScreen()
        }
    }
    composable("main"){
        MainScreen()
    }
}