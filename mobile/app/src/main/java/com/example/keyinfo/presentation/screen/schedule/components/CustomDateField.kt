package com.example.keyinfo.presentation.screen.schedule.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.keyinfo.R
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateField(
    textFieldValue: String,
    outlinedColor: Color = Color.Gray,
    containerColor: Color = Color.Transparent,
    onValueChange: (String) -> Unit,
) {
    var isDatePickerVisible by remember { mutableStateOf(false) }
    BasicTextField(modifier = Modifier
        .background(
            color = containerColor, shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp, color = outlinedColor, shape = RoundedCornerShape(8.dp)
        )
        .fillMaxWidth(0.93f),
        value = textFieldValue,
        onValueChange = {
            if (it.length <= 8 && it.isDigitsOnly()) {
                onValueChange(it)
            }
        },
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(400),
        ),
        visualTransformation = DateTransformation(),
        singleLine = true,
        enabled = true,
        decorationBox = @Composable { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                innerTextField()
                IconButton(
                    onClick = {
                        isDatePickerVisible = !isDatePickerVisible
                    },
                    modifier = Modifier
                        .padding(1.dp)
                        .width(20.dp)
                        .height(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Date picker"
                    )
                }
            }
        })
    val selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now().minusDays(3)) }
    if (isDatePickerVisible) {
        CalendarDialog(
            state = rememberUseCaseState(
                visible = true,
                onCloseRequest = { isDatePickerVisible = false }),
            config = CalendarConfig(
                yearSelection = true,
                monthSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date(
                selectedDate = selectedDate.value,
            ) { newDate ->
                selectedDate.value = newDate
                onValueChange(newDate.format(DateTimeFormatter.ofPattern("ddMMyyyy"))).toString()
            },
        )
    }
}

// https://stackoverflow.com/questions/68468942/how-to-apply-a-mask-date-mm-dd-yyyy-in-textfield-with-jetpack-compose
class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}

fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "."
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset + 1
            if (offset <= 8) return offset + 2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 2) return offset
            if (offset <= 5) return offset - 1
            if (offset <= 10) return offset - 2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}
