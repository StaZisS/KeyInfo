package com.example.keyinfo.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.keyinfo.presentation.navigation.Destinations
import com.example.keyinfo.presentation.navigation.router.AppRouter
import com.example.keyinfo.presentation.screen.main.MainScreen
import com.example.keyinfo.presentation.screen.selectauth.SelectAuthScreen

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
    }
    composable(Destinations.SELECT_AUTH_SCREEN){
        SelectAuthScreen(router = AppRouter(navController))
    }
}