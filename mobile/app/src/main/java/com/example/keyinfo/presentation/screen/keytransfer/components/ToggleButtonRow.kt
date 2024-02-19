package com.example.keyinfo.presentation.screen.keytransfer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.BottomBarColor
import com.example.keyinfo.ui.theme.CalendarDayColor

@Composable
fun ToggleButtonsRow(
    selectedButton: Int = 1,
    changeButtonClick: (Int) -> Unit
) {
    Row(modifier = Modifier.padding(top = 10.dp)) {
        val buttons = listOf("Заявки мне", "Заявки от меня")

        buttons.forEachIndexed { index, label ->
            Button(
                onClick = {
                    changeButtonClick(index)
                    when (index) {
                        0 -> Unit
                        1 -> Unit
                        else -> {}
                    }
                },
                colors = if (selectedButton == index) {
                    ButtonDefaults.buttonColors(
                        containerColor = BottomBarColor.copy(alpha = 0.1f),
                        contentColor = CalendarDayColor
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = BottomBarColor.copy(alpha = 0.01f),
                        contentColor = Color.Black.copy(alpha = 0.3f)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ToggleButtonsRowPreview() {
    ToggleButtonsRow(1, { })
}