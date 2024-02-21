package com.example.keyinfo.presentation.screen.schedule

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keyinfo.R
import com.example.keyinfo.common.Constants.CLASSES
import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.presentation.screen.schedule.components.ConfirmDialog
import com.example.keyinfo.presentation.screen.main.KeyCard
import com.example.keyinfo.presentation.screen.main.WaitScreen
import com.example.keyinfo.presentation.screen.schedule.components.BuildingsRow
import com.example.keyinfo.presentation.screen.schedule.components.Day
import com.example.keyinfo.presentation.screen.schedule.components.DaysOfWeekTitle
import com.example.keyinfo.presentation.screen.schedule.components.SearchRow
import com.example.keyinfo.presentation.screen.schedule.components.TimePickerDialog
import com.example.keyinfo.presentation.screen.schedule.components.TimeRow
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.CalendarDayColor
import com.example.keyinfo.ui.theme.SecondButtonColor
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ScheduleScreen() {
    val viewModel: ScheduleViewModel =
        viewModel(factory = ScheduleViewModelFactory(LocalContext.current))

    LaunchedEffect(Unit) {
        viewModel.getBuildings()
    }


    var confirmDialogOpened by viewModel.confirmDialogOpened
    val searchText by viewModel.searchText
    val selectedBuilding by viewModel.selectedBuilding
    var selectedDate by viewModel.selectedDate
    val selectedTimeIndex by viewModel.selectedTimeIndex
    val dialogVisible by viewModel.dialogVisible
    var currentAudience by viewModel.currentAudience


    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(50)
    val endMonth = currentMonth.plusMonths(50)
    val daysOfWeek = daysOfWeek()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )


    val allParamsSelected =
        selectedBuilding != null && selectedDate != null && selectedTimeIndex != null

    LaunchedEffect(
        viewModel.selectedBuilding.value,
        viewModel.selectedDate.value,
        viewModel.selectedTimeIndex.value
    ) {
        if (allParamsSelected) {
            viewModel.getAudience()
        }
    }
    if (dialogVisible) {
        TimePickerDialog(
            viewModel.selectedTimeIndex, viewModel.dialogVisible
        )
    }
    if (confirmDialogOpened) {
        ConfirmDialog(
            onSaveClick = {
                viewModel.reserveAudience()
                confirmDialogOpened = false
            },
            onCancelClick = { confirmDialogOpened = false },
            checkBoxChecked = viewModel.checkBoxChecked,
            untilDate = viewModel.untilDate,
            audienceInfo = currentAudience!!
        )
    }
    if (viewModel.isLoading.value && !allParamsSelected) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = AccentColor,
            trackColor = SecondButtonColor
        )
    } else {
        if (viewModel.isUnspecified.value) {
            WaitScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 16.dp, bottom = 48.dp, end = 16.dp)
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
                                selectedDate =
                                    if (selectedDate == curDay.date) null else curDay.date
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
                            viewModel.selectedBuilding, viewModel.buildings
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    AnimatedVisibility(visible = selectedBuilding != null && selectedDate != null) {
                        if (selectedTimeIndex == null) {
                            TimeRow(viewModel.dialogVisible, null)
                        } else {
                            TimeRow(viewModel.dialogVisible, CLASSES[selectedTimeIndex!!])
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
                val filteredAudiences = filterAudiences(viewModel.audiences, searchText)
                Log.d("ScheduleScreen", "filteredAudiences: $filteredAudiences")
                if (viewModel.isLoading.value) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = AccentColor,
                            trackColor = SecondButtonColor
                        )

                    }
                } else {
                    item {
                        AnimatedVisibility(visible = allParamsSelected) {
                            SearchRow(
                                text = stringResource(id = R.string.schedule_search),
                                viewModel.searchText,
                                KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                        }
                    }
                    if (filteredAudiences.isEmpty() && allParamsSelected) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Нет доступных аудиторий",
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins)),
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        items(filteredAudiences.size) {
                            val audience = filteredAudiences[it]
                            AnimatedVisibility(visible = allParamsSelected) {
                                KeyCard(
                                    audience = audience.audience,
                                    building = audience.building,
                                    startDate = audience.startTime,
                                    endDate = audience.endTime,
                                    status = audience.status,
                                    onClick = {
                                        currentAudience = audience
                                        confirmDialogOpened = true
                                    },
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun filterAudiences(audiences: List<Audience>, searchText: String): List<Audience> {
    return audiences.filter { audience ->
        audience.audience.toString()
            .contains(searchText, ignoreCase = true) || audience.building.toString()
            .contains(searchText, ignoreCase = true)
    }
}


@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun ScheduleScreenPrev() {
//    TimePickerDialog()
//    ScheduleScreen()
}