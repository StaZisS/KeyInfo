package com.example.keyinfo.presentation.screen.keytransfer.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.keyinfo.R
import com.example.keyinfo.presentation.screen.components.PairButtons
import com.example.keyinfo.presentation.screen.keytransfer.components.SmallKeyCard
import com.example.keyinfo.ui.theme.Values

@Composable
fun DeleteTransferDialog(
    audience: String,
    building: String,
    buttonState: Int,
    name: String,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onCancelClick()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Values.BasePadding),
            shape = RoundedCornerShape(Values.DialogRound),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(Values.CenterPadding),
                verticalArrangement = Arrangement.Absolute.spacedBy(Values.CenterPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(top = 2.dp))
                Text(
                    text = "Удаление заявки",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                )

                SmallKeyCard(
                    audience, building, buttonState, name
                )

                PairButtons(
                    firstLabel = stringResource(R.string.delete),
                    firstClick = { onDeleteClick() },
                    secondLabel = stringResource(R.string.cancel),
                    secondClick = { onCancelClick() },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun DeleteTransferDialogPreview(){
    DeleteTransferDialog(
        audience = "220",
        building = "2",
        buttonState = 1,
        name = "Sanya",
        onDeleteClick = { /*TODO*/ },
        onCancelClick = { }
    )
}