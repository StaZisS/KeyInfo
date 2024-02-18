package com.example.keyinfo.presentation.screen.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.keyinfo.R
import com.example.keyinfo.common.Constants.CLASSES
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.BaseButtonColor
import com.example.keyinfo.ui.theme.Values
import com.example.keyinfo.ui.theme.WhiteColor

@Composable
fun TimePickerDialog(
    selectedTimeIndex: MutableState<Int?>,
    dialogVisible: MutableState<Boolean>
) {
    var selected = selectedTimeIndex.value ?: 0

    Dialog(onDismissRequest = {
        dialogVisible.value = false
    }) {
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
                numberOfDisplayedItems = 3,
                items = CLASSES,
                initialItem = CLASSES[selected],
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    color = Color.Black
                ),
                textColor = Color.Black,
                selectedTextColor = AccentColor
            ) { index, item ->
                selected = index
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    dialogVisible.value = false
                    selectedTimeIndex.value = selected
                },
                shape = RoundedCornerShape(Values.BigRound),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Values.ButtonHeight),
                colors = BaseButtonColor
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W600
                    )
                )
            }
        }
    }
}