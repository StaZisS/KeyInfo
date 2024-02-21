package com.example.keyinfo.presentation.screen.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.domain.model.schedule.ClassTime
import com.example.keyinfo.ui.theme.AccentColor

@Composable
fun TimeRow(
    dialogVisible: MutableState<Boolean>,
    classTime: ClassTime?
) {
    Box(
        Modifier
            .background(
                AccentColor.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp)
            )
            .height(40.dp)
            .clickable { dialogVisible.value = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (classTime == null) {
                Text(
                    text = "Выберите время", style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        letterSpacing = 0.3.sp,
                        color = AccentColor.copy(alpha = 0.5f)
                    )
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.polygon_triangle),
                    contentDescription = null,
                    tint = AccentColor.copy(alpha = 0.2f)
                )
                return
            } else {
                Text(
                    text = classTime.name, style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        letterSpacing = 0.3.sp,
                        color = AccentColor
                    )
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${classTime.startTime} - ${classTime.endTime}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        letterSpacing = 0.3.sp,
                        color = AccentColor
                    )
                )
            }
        }
    }
}