package com.example.keyinfo.presentation.screen.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.keyinfo.ui.theme.CalendarDayColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onFirstClicked: () -> Unit,
    onSecondClicked: () -> Unit,
    onDismissSheet: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissSheet,
        content = {
            BottomSheetContent(onFirstClicked, onSecondClicked)
        },
        contentColor = CalendarDayColor
    )
}

@Preview
@Composable
fun SheetPrev() {
    BottomSheet({}, {}, {})
}