package com.example.keyinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.presentation.navigation.AppNavigation
import com.example.keyinfo.presentation.navigation.Screen
import com.example.keyinfo.presentation.navigation.bottombar.BottomBar
import com.example.keyinfo.ui.theme.KeyInfoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Welcome.route

            KeyInfoTheme(darkTheme = false) {
                Scaffold(
                    bottomBar = {
                        when (currentRoute) {
                            Screen.Main.route, Screen.Schedule.route, Screen.Profile.route,
                            Screen.Key.route -> {
                                BottomBar(navController = navController)
                            }

                            else -> {}
                        }
                    }) {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                    ) {
                        AppNavigation(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}