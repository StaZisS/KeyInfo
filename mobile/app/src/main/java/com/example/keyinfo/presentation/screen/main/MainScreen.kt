package com.example.keyinfo.presentation.screen.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.components.CustomIndicator
import com.example.keyinfo.presentation.screen.main.dialog.DeleteDialog
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.LightBlueColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.OffsetDateTime


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    val confirmDialogOpened by viewModel.confirmDialogOpened

    val pagerState = rememberPagerState(1)
    val pages = listOf(
        stringResource(id = R.string.main_cancelled),
        stringResource(id = R.string.main_approved),
        stringResource(id = R.string.main_pending)
    )
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(tabPositions, pagerState)
    }

    LaunchedEffect(Unit) {
        viewModel.getDeclinedRequests()
        viewModel.getAcceptedRequests()
        viewModel.getProgressRequests()
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.resetState()
        }
    }

    if (confirmDialogOpened) {
        DeleteDialog(
            id = state.currentRequest!!.application_id,
            audience = state.currentRequest!!.room_id,
            building = state.currentRequest!!.build_id,
            startDate = OffsetDateTime.parse(state.currentRequest!!.start_time),
            endDate = OffsetDateTime.parse(state.currentRequest!!.end_time),
            onDeleteClick = { viewModel.deleteRequest(state.currentRequest!!.application_id) },
            onCancelClick = { viewModel.processIntent(MainIntent.ChangeDeleteDialogState) }
        )
    }

    ScrollableTabRow(
        containerColor = Color.Transparent,
        divider = {},
        selectedTabIndex = pagerState.currentPage,
        indicator = indicator,
        modifier = Modifier
            .padding(top = 16.dp, bottom = 48.dp)
    ) {
        pages.forEachIndexed { index, title ->
            Tab(text = {
                if (pagerState.currentPage != index) {
                    Box(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = title, style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 1.1.sp,
                                fontFamily = FontFamily(Font(R.font.poppins)),
                                color = LightBlueColor
                            )
                        )
                    }
                } else {
                    Text(
                        text = title, style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 1.1.sp,
                            fontFamily = FontFamily(Font(R.font.poppins)),
                            color = AccentColor
                        )
                    )
                }
            }, selected = pagerState.currentPage == index, onClick = {})
        }
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        count = pages.size,
        state = pagerState,
    ) { page ->
        LazyColumn(
            modifier = Modifier.padding(top = 90.dp, start = 16.dp, end = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Log.d("MainScreen", "isLoading: ${state.isLoading}")

            when (page) {
                0 -> { // Отклоенные
                    if (state.isLoading && state.declinedRequests.isEmpty()) {
                        items(4) {
                            KeyCardShimmer()
                        }
                    } else {
                        item {
                            if (state.declinedRequests.isNotEmpty()) {
                                state.declinedRequests.forEachIndexed { index, request ->
                                    KeyCard(
                                        audience = request.room_id,
                                        building = request.build_id,
                                        startDate = OffsetDateTime.parse(request.start_time),
                                        endDate = OffsetDateTime.parse(request.end_time)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            } else {
                                PageEmptyScreen(
                                    label = stringResource(id = R.string.main_empty),
                                    description = stringResource(id = R.string.main_empty_description)
                                )
                            }
                        }
                    }
                }

                1 -> { // Принятые
//                    Log.d("MainScreen", "isLoading now: ${viewModel.isLoading.value} ${state.acceptedRequests}")
                    if (state.isLoading && state.acceptedRequests.isEmpty()) {
                        items(4) {
                            KeyCardShimmer()
                        }
                    } else {
                        item {
                            if (state.acceptedRequests.isNotEmpty()) {
                                state.acceptedRequests.forEachIndexed { index, request ->
                                    KeyCard(
                                        audience = request.room_id,
                                        building = request.build_id,
                                        startDate = OffsetDateTime.parse(request.start_time),
                                        endDate = OffsetDateTime.parse(request.end_time)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            } else {
                                PageEmptyScreen(
                                    label = stringResource(id = R.string.main_empty),
                                    description = stringResource(id = R.string.main_empty_description)
                                )
                            }
                        }
                    }
                }

                2 -> { // В ожидании
                    if (state.isLoading && state.processRequests.isEmpty()) {
                        items(4) {
                            KeyCardShimmer()
                        }
                    } else {
                        item {
                            if (state.processRequests.isNotEmpty()) {
                                state.processRequests.forEachIndexed { index, request ->
                                    KeyCard(
                                        audience = request.room_id,
                                        building = request.build_id,
                                        startDate = OffsetDateTime.parse(request.start_time),
                                        endDate = OffsetDateTime.parse(request.end_time),
                                        onClick = {
                                            viewModel.processIntent(MainIntent.SetNewRequest(request))
                                            viewModel.processIntent(MainIntent.ChangeDeleteDialogState)
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            } else {
                                PageEmptyScreen(
                                    label = stringResource(id = R.string.main_empty),
                                    description = stringResource(id = R.string.main_empty_description)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}