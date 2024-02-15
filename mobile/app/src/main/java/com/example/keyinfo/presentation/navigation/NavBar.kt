package com.example.keyinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keyinfo.presentation.navigation.bottombar.BottomBarScreen
import com.example.keyinfo.presentation.screen.main.MainScreen
import com.example.keyinfo.presentation.screen.profile.ProfileScreen

@Composable
fun NavBar(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            MainScreen()
        }
        composable(route = BottomBarScreen.Timetable.route) {
            ProfileScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen()
        }
        composable(route = "back"){
            Navigation()
        }
    }
}