package com.example.keyinfo.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.components.CustomIndicator
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.LightBlueColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val pagerState = rememberPagerState(1)
    val pages = listOf(
        stringResource(id = R.string.main_cancelled),
        stringResource(id = R.string.main_approved),
        stringResource(id = R.string.main_pending)
    )
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(tabPositions, pagerState)
    }

    ScrollableTabRow(
        containerColor = Color.Transparent,
        divider = {},
        selectedTabIndex = pagerState.currentPage,
        indicator = indicator,
        modifier = Modifier
            .padding(top = 16.dp)
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
            modifier = Modifier.padding(top = 90.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (page) {
                0 -> { // Отклоенные
                    items(3) {
                        KeyCard()
                    }
                }

                1 -> { // Принятые
                    items(1) {
                        KeyCard()
                    }
                }

                2 -> { // В ожидании
                    // if no items
                    item { PageEmptyScreen() }
//                    items(0) {
//                        KeyCard()
//                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPrev() {
//    PageEmptyScreen()
    MainScreen(rememberNavController())
}