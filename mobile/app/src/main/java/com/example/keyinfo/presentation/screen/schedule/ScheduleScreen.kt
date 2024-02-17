package com.example.keyinfo.presentation.screen.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.main.KeyCard
import com.example.keyinfo.presentation.screen.schedule.components.BuildingsRow
import com.example.keyinfo.presentation.screen.schedule.components.Day
import com.example.keyinfo.presentation.screen.schedule.components.DaysOfWeekTitle
import com.example.keyinfo.presentation.screen.schedule.components.SearchRow
import com.example.keyinfo.presentation.screen.schedule.components.TimePickerDialog
import com.example.keyinfo.presentation.screen.schedule.components.TimeRow
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
    // todo disable old dates selection (before today)
    val items = listOf(
        ClassTime("1 пара", "8:45 - 10:20"),
        ClassTime("2 пара", "10:35 - 12:10"),
        ClassTime("3 пара", "12:25 - 14:00"),
        ClassTime("4 пара", "14:45 - 16:20"),
        ClassTime("5 пара", "16:35 - 18:10"),
        ClassTime("6 пара", "18:25 - 20:00"),
        ClassTime("7 пара", "20:15 - 21:50"),
    )
    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(50)
    val endMonth = currentMonth.plusMonths(50)
    val daysOfWeek = daysOfWeek()
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val searchText = remember { mutableStateOf("") }
    val dialogVisible = remember { mutableStateOf(false) }
    val selectedBuildingIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val selectedTimeIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val allParamsSelected =
        selectedBuildingIndex.value != null && selectedDate != null && selectedTimeIndex.value != null
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
        }
        item {
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
            Spacer(modifier = Modifier.height(5.dp))
        }
        item {
            AnimatedVisibility(visible = selectedDate != null) {
                BuildingsRow(
                    selectedBuildingIndex, listOf(
                        1, 2, 3, 4, 5, 6, 7, 8, 9, 10
                    )
                )
            }
            Spacer(Modifier.height(10.dp))
        }
        item {
            AnimatedVisibility(visible = selectedBuildingIndex.value != null && selectedDate != null) {
                if (selectedTimeIndex.value == null) {
                    TimeRow(dialogVisible, null)
                } else {
                    TimeRow(dialogVisible, items[selectedTimeIndex.value!!])
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        item {
            AnimatedVisibility(visible = allParamsSelected) {
                SearchRow(searchText)
            }
        }
        items(5) {
            AnimatedVisibility(visible = allParamsSelected) {
                KeyCard()
            }
        }
    }
}


data class ClassTime(
    var name: String, var time: String
)

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun ScheduleScreenPrev() {
//    TimePickerDialog()
//    ScheduleScreen()
}