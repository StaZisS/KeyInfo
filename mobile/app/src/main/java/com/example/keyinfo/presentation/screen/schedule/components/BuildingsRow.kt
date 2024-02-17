package com.example.keyinfo.presentation.screen.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
fun BuildingsRow(
    selectedBuilding: MutableState<Int?>,
    buildings: List<Int>
) {
    LazyRow {
        items(buildings.size) { index ->
            val building = buildings[index]
            Box(modifier = Modifier
                .padding(10.dp)
                .clickable {
                    selectedBuilding.value = index
                }) {
                Text(
                    text = "Корпус $building", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        color = if (selectedBuilding.value == index) AccentColor else AccentColor.copy(
                            alpha = 0.2f
                        )
                    ), modifier = Modifier
                        .background(
                            color = if (selectedBuilding.value == index) AccentColor.copy(alpha = 0.1f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp)
                )
            }
        }
    }
}

