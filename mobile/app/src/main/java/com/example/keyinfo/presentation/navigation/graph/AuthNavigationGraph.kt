package com.example.keyinfo.presentation.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.keyinfo.presentation.screen.registration.RegistrationFirstScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationSecondScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.navigation.Destinations

const val AUTH_ROUTE = "auth_root"

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    registrationViewModel: RegistrationViewModel
) {
    navigation(
        startDestination = Destinations.SPLASH_SCREEN,
        route = AUTH_ROUTE
    ) {
        composable(Destinations.REGISTRATION_FIRST_SCREEN) {
            RegistrationFirstScreen(registrationViewModel)
        }
        composable(Destinations.REGISTRATION_SECOND_SCREEN) {
            RegistrationSecondScreen(registrationViewModel)
        }
    }
    composable(Destinations.REGISTRATION_FIRST_SCREEN) {
        RegistrationFirstScreen(registrationViewModel)
    }
}