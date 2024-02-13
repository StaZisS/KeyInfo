package com.example.keyinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.navigation.graph.authNavigationGraph
import com.example.keyinfo.presentation.navigation.router.AppRouter
import com.example.keyinfo.presentation.screen.login.LoginViewModel

const val ROOT_ROUTE = "root"

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val registrationViewModel = RegistrationViewModel(
         context = LocalContext.current,
         router = AppRouter(navController)
    )

    val loginViewModel = LoginViewModel(
        context = LocalContext.current,
        router = AppRouter(navController)
    )

    NavHost(
        navController = navController,
        startDestination = Destinations.SELECT_AUTH_SCREEN,
        route = ROOT_ROUTE
    ) {
        authNavigationGraph(
            navController = navController,
            registrationViewModel = registrationViewModel,
            loginViewModel = loginViewModel
        )
    }
}