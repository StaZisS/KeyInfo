package com.example.keyinfo.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.keyinfo.presentation.screen.registration.RegistrationFirstScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationSecondScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.navigation.Destinations
import com.example.keyinfo.presentation.screen.login.LoginScreen
import com.example.keyinfo.presentation.screen.login.LoginViewModel

const val AUTH_ROUTE = "auth_root"

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    registrationViewModel: RegistrationViewModel,
    loginViewModel: LoginViewModel
) {
    navigation(
        startDestination = Destinations.SPLASH_SCREEN,
        route = AUTH_ROUTE
    ) {
        composable(Destinations.REGISTRATION_FIRST_SCREEN) {
            RegistrationFirstScreen(viewModel = registrationViewModel)
        }
        composable(Destinations.REGISTRATION_SECOND_SCREEN) {
            RegistrationSecondScreen(viewModel = registrationViewModel)
        }
        composable(Destinations.LOGIN_SCREEN){
            LoginScreen(viewModel = loginViewModel)
        }
    }
    composable(Destinations.REGISTRATION_FIRST_SCREEN) {
        RegistrationFirstScreen(viewModel = registrationViewModel)
    }
}