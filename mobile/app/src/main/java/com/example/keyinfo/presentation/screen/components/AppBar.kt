package com.example.keyinfo.presentation.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(
    toAnotherScreen: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        IconButton(
            onClick = { toAnotherScreen() },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }
    }
}