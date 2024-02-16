package com.example.keyinfo.presentation.navigation.router

import androidx.navigation.NavController
import com.example.keyinfo.presentation.navigation.Screen

class AppRouter(
    private val navController: NavController
) {
    fun toLogin() {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Welcome.route)
        }
    }

    fun toRegistration() {
        navController.navigate(Screen.Registration.route) {
            popUpTo(Screen.Welcome.route)
        }
    }

    fun toAuth() {
        navController.navigate(Screen.Welcome.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

    fun toPasswordRegistration() {
        navController.navigate(Screen.RegistrationPwd.route)
    }

    fun toMain() {
        navController.navigate(Screen.Main.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}