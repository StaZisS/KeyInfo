package com.example.keyinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keyinfo.presentation.navigation.router.BottomBarRouter
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferScreen
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferViewModel
import com.example.keyinfo.presentation.screen.login.LoginScreen
import com.example.keyinfo.presentation.screen.login.LoginViewModel
import com.example.keyinfo.presentation.screen.main.MainScreen
import com.example.keyinfo.presentation.screen.main.MainViewModel
import com.example.keyinfo.presentation.screen.profile.ProfileScreen
import com.example.keyinfo.presentation.screen.profile.ProfileViewModel
import com.example.keyinfo.presentation.screen.registration.RegistrationFirstScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationSecondScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.screen.schedule.ScheduleScreen
import com.example.keyinfo.presentation.screen.selectauth.SelectAuthScreen
import com.example.keyinfo.presentation.screen.splash.LoadingScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    registrationViewModel: RegistrationViewModel,
    loginViewModel: LoginViewModel,
    keyViewModel: KeyTransferViewModel,
    profileViewModel: ProfileViewModel,
    mainViewModel: MainViewModel
) {
    NavHost(
        navController,
        startDestination = Screen.Splash.route,
    ) {

        composable(Screen.Splash.route) {
            LoadingScreen(navController = navController)
        }
        composable(Screen.Welcome.route) {
            SelectAuthScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(viewModel = loginViewModel)
        }
        composable(Screen.Registration.route) {
            RegistrationFirstScreen(
                viewModel = registrationViewModel
            )
        }
        composable(Screen.RegistrationPwd.route) {
            RegistrationSecondScreen(
                viewModel = registrationViewModel
            )
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen()
        }
        composable(Screen.Main.route) {
            MainScreen(viewModel = mainViewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(profileViewModel) { BottomBarRouter(navController).toAuth() }
        }
        composable(Screen.Key.route) {
            KeyTransferScreen(
                viewModel = keyViewModel
            )
        }
    }
}
