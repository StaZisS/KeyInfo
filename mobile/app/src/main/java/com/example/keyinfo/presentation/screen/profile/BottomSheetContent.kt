package com.example.keyinfo.presentation.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.WhiteColor

@Composable
fun BottomSheetContent(
    onFirstClicked: () -> Unit,
    onSecondClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Заказать пиццу",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins)
                    ),
                    textAlign = TextAlign.Center,
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onFirstClicked()
                    },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6900),
                        disabledContainerColor = Color(0xFFFF6900)
                    ),
                ) {
                    Icon(
                        painterResource(id = R.drawable.pizza),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        tint = WhiteColor
                    )
                    Text(
                        "Додо Пицца", style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        onSecondClicked()
                    },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFCC1B),
                        disabledContainerColor = Color(0xFFFFCC1B)
                    ),
                ) {
                    Icon(
                        painterResource(id = R.drawable.pizza),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        tint = WhiteColor
                    )
                    Text(
                        "Make Love", style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}