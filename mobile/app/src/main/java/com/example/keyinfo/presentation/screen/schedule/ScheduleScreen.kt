package com.example.keyinfo.presentation.screen.schedule

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.main.KeyCard
import com.example.keyinfo.ui.theme.CalendarDayColor
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


// todo add button for month and year selection
@Composable
fun ScheduleScreen() {
    val items = listOf(
        ClassTime("1 пара", "8:00-9:35"),
        ClassTime("2 пара", "9:45-11:20"),
        ClassTime("3 пара", "11:30-13:05"),
        ClassTime("4 пара", "14:45-16:20"),
        ClassTime("5 пара", "16:30-18:05"),
        ClassTime("6 пара", "18:15-19:50"),
        ClassTime("7 пара", "20:00-21:35")
    )
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }

    val daysOfWeek = remember { daysOfWeek() }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val searchText = remember { mutableStateOf("") }
    val dialogVisible = remember { mutableStateOf(false) }
    val selectedTimeIndex = remember { mutableIntStateOf(3) }
    if (dialogVisible.value) {
        TimePickerDialog(dialogVisible, selectedTimeIndex, items)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)
    ) {
        item {
            Text(text = "${
                state.lastVisibleMonth.yearMonth.month.getDisplayName(
                    TextStyle.FULL_STANDALONE, Locale.getDefault()
                )
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            } ${state.lastVisibleMonth.yearMonth.year}",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    color = CalendarDayColor
                ),
                modifier = Modifier.padding(start = 14.dp))
            Spacer(modifier = Modifier.height(28.dp))
            HorizontalCalendar(
                contentPadding = PaddingValues(0.dp),
                state = state,
                modifier = Modifier.fillMaxWidth(),
                dayContent = { day ->
                    Day(day, isSelected = selectedDate == day.date) { curDay ->
                        selectedDate = if (selectedDate == curDay.date) null else curDay.date
                    }
                },
                monthHeader = {
                    DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            BuildingsRow()
            Spacer(Modifier.height(15.dp))
        }
        item {
            TimeRow(dialogVisible, items[selectedTimeIndex.intValue])
            Spacer(Modifier.height(15.dp))
        }
        item {
            SearchRow(searchText)
        }
        items(10) {
            KeyCard()
        }
    }
}


data class Building(
    val name: String, var isSelected: Boolean
)

data class ClassTime(
    var name: String, var time: String
)

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun ScheduleScreenPrev() {
//    TimePickerDialog()
    ScheduleScreen()
}