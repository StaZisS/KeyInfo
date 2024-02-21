package com.example.keyinfo.presentation.navigation.router

import androidx.navigation.NavController
import com.example.keyinfo.presentation.navigation.Screen

class BottomBarRouter(
    private val navController: NavController
) {
    fun toKey(){
        navController.navigate(Screen.Key.route)
    }

    fun toProfile(){
        navController.popBackStack()
    }

    fun toAuth() {
        navController.navigate(Screen.Welcome.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
}