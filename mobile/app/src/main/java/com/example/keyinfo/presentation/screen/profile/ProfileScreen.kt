package com.example.keyinfo.presentation.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.LightBlueColor
import com.example.keyinfo.ui.theme.TextButtonColor


@Composable
fun ProfileScreen(
    clickOnKey: () -> Unit,
    clickOnLeave: () -> Unit
) {
    val name = "John Doe"
    Box(Modifier.padding(16.dp)) {
        IconButton(
            onClick = { clickOnKey() }, modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.key),
                contentDescription = null,
                Modifier.size(20.dp)
            )
        }

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(112.dp),
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = name,
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight(700),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = "Software Developer",
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular)
                    )
                ),
                color = LightBlueColor
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextButton(
                onClick = { clickOnLeave() },
                colors = TextButtonColor,
                modifier = Modifier.fillMaxWidth(0.5f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Выйти из аккаунта",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight(600),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen({}, {})
}