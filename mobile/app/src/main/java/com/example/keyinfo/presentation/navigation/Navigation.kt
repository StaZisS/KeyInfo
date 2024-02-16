package com.example.keyinfo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.keyinfo.presentation.navigation.router.AppRouter
import com.example.keyinfo.presentation.navigation.router.BottomBarRouter
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferScreen
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferViewModel
import com.example.keyinfo.presentation.screen.login.LoginScreen
import com.example.keyinfo.presentation.screen.login.LoginViewModel
import com.example.keyinfo.presentation.screen.main.MainScreen
import com.example.keyinfo.presentation.screen.profile.ProfileScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationFirstScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationSecondScreen
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.screen.schedule.ScheduleScreen
import com.example.keyinfo.presentation.screen.selectauth.SelectAuthScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    val registrationViewModel = RegistrationViewModel(
        context = LocalContext.current,
        router = AppRouter(navController)
    )
    val loginViewModel = LoginViewModel(
        context = LocalContext.current,
        router = AppRouter(navController)
    )
    val keyViewModel = KeyTransferViewModel()

    NavHost(
        navController,
        startDestination = Screen.Welcome.route,
    ) {

        composable(Screen.Splash.route) {
        }
        composable(Screen.Welcome.route) {
            SelectAuthScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
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
            ScheduleScreen(navController = navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                clickOnKey = { BottomBarRouter(navController).toKey() },
                clickOnLeave =  { BottomBarRouter(navController).toAuth() }
            )
        }
        composable(Screen.Key.route){
            KeyTransferScreen(
                viewModel = keyViewModel,
                clickOnBar = { BottomBarRouter(navController).toProfile() }
            )
        }
    }
}
