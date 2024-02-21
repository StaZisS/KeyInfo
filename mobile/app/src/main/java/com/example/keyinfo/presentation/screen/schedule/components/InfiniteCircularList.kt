package com.example.keyinfo.presentation.screen.schedule.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.keyinfo.domain.model.schedule.ClassTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteCircularList(
    itemHeight: Dp,
    numberOfDisplayedItems: Int = 3,
    items: List<ClassTime>,
    initialItem: ClassTime,
    itemScaleFact: Float = 1.5f,
    textStyle: androidx.compose.ui.text.TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    onItemSelected: (index: Int, item: ClassTime) -> Unit = { _, _ -> }
) {
    val itemHalfHeight = LocalDensity.current.run { itemHeight.toPx() / 2f }
    val scrollState = rememberLazyListState(0)
    var lastSelectedIndex by remember {
        mutableIntStateOf(0)
    }
    var itemsState by remember {
        mutableStateOf(items)
    }
    LaunchedEffect(items) {
        var targetIndex = items.indexOf(initialItem) - 1
        targetIndex += ((Int.MAX_VALUE / 2) / items.size) * items.size
        itemsState = items
        lastSelectedIndex = targetIndex
        scrollState.scrollToItem(targetIndex)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight * numberOfDisplayedItems),
        state = scrollState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = scrollState
        )
    ) {
        items(count = Int.MAX_VALUE, itemContent = { i ->
            val item = itemsState[i % itemsState.size]
            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        val y = coordinates.positionInParent().y - itemHalfHeight
                        val parentHalfHeight =
                            (coordinates.parentCoordinates?.size?.height ?: 0) / 2f
                        val isSelected =
                            (y > parentHalfHeight - itemHalfHeight && y < parentHalfHeight + itemHalfHeight)
                        if (isSelected && lastSelectedIndex != i) {
                            onItemSelected(i % itemsState.size, item)
                            lastSelectedIndex = i
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = if (lastSelectedIndex == i) {
                            textStyle.fontSize * itemScaleFact
                        } else {
                            textStyle.fontSize
                        }
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = item.startTime.toString() + " - " + item.endTime.toString(),
                        style = textStyle,
                        color = if (lastSelectedIndex == i) {
                            selectedTextColor
                        } else {
                            textColor
                        },
                        fontSize = if (lastSelectedIndex == i) {
                            textStyle.fontSize * itemScaleFact
                        } else {
                            textStyle.fontSize
                        }
                    )
                }
            }
        })
    }
}