package com.example.keyinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.presentation.navigation.AppNavigation
import com.example.keyinfo.presentation.navigation.Screen
import com.example.keyinfo.presentation.navigation.bottombar.BottomBar
import com.example.keyinfo.presentation.navigation.router.AppRouter
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferViewModel
import com.example.keyinfo.presentation.screen.keytransfer.KeyTransferViewModelFactory
import com.example.keyinfo.presentation.screen.login.LoginViewModel
import com.example.keyinfo.presentation.screen.login.LoginViewModelFactory
import com.example.keyinfo.presentation.screen.main.MainViewModel
import com.example.keyinfo.presentation.screen.main.MainViewModelFactory
import com.example.keyinfo.presentation.screen.profile.ProfileViewModel
import com.example.keyinfo.presentation.screen.profile.ProfileViewModelFactory
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModel
import com.example.keyinfo.presentation.screen.registration.RegistrationViewModelFactory
import com.example.keyinfo.ui.theme.KeyInfoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Welcome.route
            val ctx = LocalContext.current
            val registrationViewModel: RegistrationViewModel by viewModels(
                factoryProducer = {
                    RegistrationViewModelFactory(
                        context = ctx,
                        router = AppRouter(navController)
                    )
                }
            )
            val loginViewModel: LoginViewModel by viewModels(
                factoryProducer = {
                    LoginViewModelFactory(
                        context = ctx,
                        router = AppRouter(navController)
                    )
                }
            )
            val keyViewModel: KeyTransferViewModel by viewModels(
                factoryProducer = {
                    KeyTransferViewModelFactory(
                        context = ctx
                    )
                })
            val profileViewModel: ProfileViewModel by viewModels {
                ProfileViewModelFactory(
                    context = ctx
                )
            }

            val mainViewModel: MainViewModel by viewModels {
                MainViewModelFactory(
                    context = ctx
                )
            }

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
                            registrationViewModel = registrationViewModel,
                            loginViewModel = loginViewModel,
                            keyViewModel = keyViewModel,
                            profileViewModel = profileViewModel,
                            mainViewModel = mainViewModel
                        )
                    }
                }
            }
        }
    }
}