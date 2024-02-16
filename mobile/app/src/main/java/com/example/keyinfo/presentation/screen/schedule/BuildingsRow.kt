package com.example.keyinfo.presentation.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.AccentColor

@Composable
fun BuildingsRow() {
    val list = remember {
        mutableStateListOf(
            Building("Корпус 1", false),
            Building("Корпус 2", true),
            Building("Корпус 3", false),
            Building("Корпус 4", false),
            Building("Корпус 5", false),
        )
    }
    LazyRow {
        items(list.size) { index ->
            val building = list[index]
            Box(modifier = Modifier
                .padding(10.dp)
                .clickable {
                    list.forEach { it.isSelected = false }
                    list[index] = list[index].copy(isSelected = true)
                }) {
                Text(
                    text = building.name, style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        color = if (building.isSelected) AccentColor else AccentColor.copy(alpha = 0.2f)
                    ), modifier = Modifier
                        .background(
                            color = if (building.isSelected) AccentColor.copy(alpha = 0.1f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp)
                )
            }
        }
    }
}