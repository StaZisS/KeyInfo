package com.example.keyinfo.presentation.screen.keytransfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.components.AppBar
import com.example.keyinfo.presentation.screen.keytransfer.components.SmallKeyCard
import com.example.keyinfo.presentation.screen.keytransfer.components.ToggleButtonsRow
import com.example.keyinfo.ui.theme.Values

@Composable
fun KeyTransferScreen(viewModel: KeyTransferViewModel){
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Values.BasePadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppBar{}

        Text(
            text = stringResource(R.string.key_tranfser),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = Values.SpaceBetweenObjects
            )
        )

        ToggleButtonsRow(
            selectedButton = state.currentButton,
            changeButtonClick = { viewModel.processIntent(KeyTransferIntent.ClickOnButton(it)) }
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = 20.dp)
        ){
            items(10){
                SmallKeyCard()
            }
        }
    }
}