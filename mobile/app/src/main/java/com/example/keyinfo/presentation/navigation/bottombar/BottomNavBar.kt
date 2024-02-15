package com.example.keyinfo.presentation.navigation.bottombar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.presentation.navigation.NavBar

@Composable
fun BottomNavBar() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        Modifier.padding(it)
        NavBar(navController)
    }
}