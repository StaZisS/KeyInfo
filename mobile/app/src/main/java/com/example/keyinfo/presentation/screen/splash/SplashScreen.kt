package com.example.keyinfo.presentation.screen.splash

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.keyinfo.R
import com.example.keyinfo.domain.model.ApiResult
import com.example.keyinfo.presentation.navigation.Screen

@Composable
fun LoadingScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    val viewModel: LoadingViewModel = viewModel(factory = LoadingViewModelFactory(ctx))

    LaunchedEffect(Unit) {
        viewModel.checkUser {
            when (it) {
                is ApiResult.Success -> navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }

                is ApiResult.Error -> {
                    if (it.message != null) {
                        Toast.makeText(
                            ctx,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.d("LoadingScreen", "Error: ${it.message}")
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Preview
@Composable
fun LoadingPreview() {
    LoadingScreen(navController = NavHostController(LocalContext.current))
}
