package com.example.keyinfo.presentation.screen.keytransfer

import androidx.compose.foundation.clickable
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
import com.example.keyinfo.presentation.screen.keytransfer.components.SmallKeyCard
import com.example.keyinfo.presentation.screen.keytransfer.components.ToggleButtonsRow
import com.example.keyinfo.presentation.screen.keytransfer.dialog.DeleteTransferDialog
import com.example.keyinfo.presentation.screen.keytransfer.dialog.SelectPersonDialog
import com.example.keyinfo.presentation.screen.keytransfer.dialog.TransferDialog
import com.example.keyinfo.presentation.screen.main.PageEmptyScreen
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.LightBlueColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun KeyTransferScreen(
    viewModel: KeyTransferViewModel
) {
    val state by viewModel.state.collectAsState()

    val myKeysTransferDialogOpen by viewModel.myKeysTransferDialogOpen
    val transferDialogOpen by viewModel.transferDialogOpened
    
    LaunchedEffect(Unit) {
        viewModel.getUserKeys()
    }

    val pagerState = rememberPagerState(1)
    val pages = listOf(
        "Передачи ключей",
        "Мои ключи"
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
                0 -> { // Передача ключа
                    item {
                        ToggleButtonsRow(
                            selectedButton = state.currentButton,
                            changeButtonClick = {
                                viewModel.processIntent(
                                    KeyTransferIntent.ClickOnButton(
                                        it
                                    )
                                )
                            }
                        )
                    }
                    item {
                        if (state.currentButton == 0) {
                            if (state.fromMeRequests.isNotEmpty()){
                                state.fromMeRequests.forEach{ transfer ->
                                    Box (
                                        modifier = Modifier.clickable {  }
                                    ){
                                        SmallKeyCard(
                                            audience = transfer.room_id.toString(),
                                            building = transfer.build_id.toString(),
                                            buttonState = 0,
                                            name = transfer.owner_name
                                        )
                                    }
                                }
                            } else {
                                PageEmptyScreen(
                                    label = "У вас нет заявок",
                                    description = "К сожалению вам никто не хочет передать свой ключ"
                                )
                            }
                        } else {
                            if (state.myRequests.isNotEmpty()){
                                state.myRequests.forEach{ transfer ->
                                    Box(
                                        modifier = Modifier.clickable {  }
                                    ) {
                                        SmallKeyCard(
                                            audience = transfer.room_id.toString(),
                                            building = transfer.build_id.toString(),
                                            buttonState = 1,
                                            name = transfer.receiver_name
                                        )
                                    }
                                }
                            } else {
                                PageEmptyScreen(
                                    label = "Вы никому не отправили заявок",
                                    description = "К сожалению вы никому не хотите передать свой ключ"
                                )
                            }
                        }
                    }
                }

                1 -> {
                    item {
                        if (state.myKeys.isNotEmpty()) {
                            state.myKeys.forEach { key ->
                                Box(
                                    modifier = Modifier.clickable {  }
                                ){
                                    SmallKeyCard(
                                        audience = key.room.toString(),
                                        building = key.build.toString()
                                    )
                                }
                            }
                        } else {
                            PageEmptyScreen(
                                label = stringResource(id = R.string.keys_empty),
                                description = stringResource(id = R.string.main_empty_description)
                            )
                        }
                    }
                }
            }
        }
    }
    
    if (myKeysTransferDialogOpen){
        SelectPersonDialog(
            onCardClick = { },
            searchText = "1"
        )
    }

    if (transferDialogOpen){
        if (state.currentButton == 0){
            TransferDialog(
                audience = "220",
                building = "2",
                name = "Sanya",
                onAcceptClick = { },
                onRejectClick = { }
            )
        } else {
            DeleteTransferDialog(
                audience = "220",
                building = "2",
                buttonState = 1,
                name = "Sanya",
                onDeleteClick = { /*TODO*/ },
                onCancelClick = { }
            )
        }
    }
}