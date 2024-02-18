package com.example.keyinfo.presentation.screen.keytransfer.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.keyinfo.domain.model.schedule.Audience
import com.example.keyinfo.domain.model.schedule.AudienceStatus
import com.example.keyinfo.presentation.screen.components.PairButtons
import com.example.keyinfo.presentation.screen.main.KeyCard
import com.example.keyinfo.presentation.screen.schedule.components.CustomDateField
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.Values.BasePadding
import com.example.keyinfo.ui.theme.Values.CenterPadding
import com.example.keyinfo.ui.theme.Values.DialogRound
import java.time.OffsetDateTime

@Composable
fun ConfirmDialog(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    checkBoxChecked: MutableState<Boolean>,
    untilDate: MutableState<String>,
    audienceInfo: Audience
) {
    Dialog(
        onDismissRequest = {
            onCancelClick()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
            shape = RoundedCornerShape(DialogRound),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(CenterPadding),
                verticalArrangement = Arrangement.Absolute.spacedBy(CenterPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(top = 2.dp))
                Text(
                    text = stringResource(id = R.string.reserve),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    ),
                )

                KeyCard(
                    audience = audienceInfo.audience,
                    building = audienceInfo.building,
                    startDate = audienceInfo.startTime,
                    endDate = audienceInfo.endTime,
                    status = audienceInfo.status
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = checkBoxChecked.value,
                        onCheckedChange = { checkBoxChecked.value = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentColor,
                            uncheckedColor = AccentColor
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.dublicate),
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Center,
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
                if (checkBoxChecked.value) {
                    CustomDateField(
                        onValueChange = {
                            untilDate.value = it
                        },
                        textFieldValue = untilDate.value,
                        outlinedColor = AccentColor,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                PairButtons(
                    firstLabel = stringResource(R.string.confirm),
                    firstClick = {
                        if (checkBoxChecked.value) {
                            if (untilDate.value.isEmpty()) {
                                return@PairButtons
                            }
                        }
                        onSaveClick()
                    },
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
fun ConfirmDialogPreview() {
    val checkBoxChecked = remember { mutableStateOf(true) }
    ConfirmDialog(
        onSaveClick = { },
        onCancelClick = { },
        untilDate = remember { mutableStateOf("") },
        checkBoxChecked = checkBoxChecked,
        audienceInfo = Audience(
            audience = 101,
            building = 2,
            startTime = OffsetDateTime.now(),
            endTime = OffsetDateTime.now(),
            status = AudienceStatus.FREE
        )
    )
}