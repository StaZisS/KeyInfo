package com.example.keyinfo.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val StatusBarColor = Color(0xFF0F0F0F)
val BackgroundColor = Color(0xFF1D1D1D)
val AccentColor = Color(0xFFFC315E)
val WhiteColor = Color(0xFFFFFFFF)
val SecondButtonColor = Color(0xFF292929)
val GrayColor = Color(0xFFC4C8CC)
val DarkGrayColor = Color(0xFF5E5E5E)
val SuperDarkGrayColor = Color(0xFF767680)
val ErrorAccentColor = Color(0xFFE64646)
val BottomBarColor = Color(0xFF161616)
val ChipColor = Color(0xFF404040)
val DisabledButtonColor = Color(0xFFFC315E)
val Gray400Color = Color(0xFF909499)
val YellowStarColor = Color(0xFFFFD54F)
val MenuGrayColor = Color(0xFF55595D)
val RedColor = Color(0xFFE64646)

val BaseButtonColor = ButtonColors(
    containerColor = AccentColor,
    contentColor = WhiteColor,
    disabledContainerColor = DisabledButtonColor.copy(alpha = 0.24f),
    disabledContentColor = WhiteColor.copy(alpha = 0.24f)
)