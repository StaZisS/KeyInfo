package com.example.keyinfo.presentation.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R

@Composable
fun PageEmptyScreen(
    label: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sad),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 35.dp)
        )

        Text(
            text = label, style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(700),
                textAlign = TextAlign.Center,
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description, style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center,
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(35.dp))
    }
}