package com.example.keyinfo.presentation.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.WhiteColor

@Composable
fun TimePickerDialog(
    dialogVisible: MutableState<Boolean>,
    selectedTimeIndex: MutableState<Int> = mutableIntStateOf(3),
    items: List<ClassTime>
) {
    Dialog(onDismissRequest = { dialogVisible.value = false }) {
        Column(
            modifier = Modifier
                .background(
                    color = WhiteColor.copy(alpha = 0.95f), shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfiniteCircularList(
                itemHeight = 40.dp,
                items = items,
                initialItem = items[selectedTimeIndex.value],
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    color = Color.Black
                ),
                textColor = Color.Black,
                selectedTextColor = AccentColor
            ) { index, item ->
                selectedTimeIndex.value = index
            }
        }

    }
}