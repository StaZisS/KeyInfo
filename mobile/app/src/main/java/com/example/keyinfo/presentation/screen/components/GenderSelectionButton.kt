package com.example.keyinfo.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.ChipColor
import com.example.keyinfo.ui.theme.LightGrayColor
import com.example.keyinfo.ui.theme.SecondButtonColor
import com.example.keyinfo.ui.theme.SuperDarkGrayColor
import com.example.keyinfo.ui.theme.Values.MicroPadding
import com.example.keyinfo.ui.theme.Values.MiddlePadding
import com.example.keyinfo.ui.theme.Values.MiddleRound

@Composable
fun GenderSelectionButton(
    updateRole: (String) -> Unit,
    state: String,
    modifier: Modifier
) {
    val man = stringResource(R.string.man)
    val woman = stringResource(R.string.woman)

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.gender),
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W500)
            )

            Row(
                modifier = Modifier
                    .padding(top = MiddlePadding)
                    .background(
                        color = LightGrayColor,
                        shape = RoundedCornerShape(MiddleRound)
                    )
            ) {
                Button(
                    onClick = {
                        updateRole("MALE")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = MicroPadding,
                            top = MicroPadding,
                            bottom = MicroPadding,
                            end = 0.dp
                        ),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state == "MALE")
                            AccentColor else LightGrayColor,
                        contentColor = if (state == "MALE")
                            Color.White else ChipColor
                    )
                ) {
                    Text(man)
                }

                Button(
                    onClick = {
                        updateRole("FEMALE")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
                        .padding(
                            start = 0.dp,
                            top = MicroPadding,
                            bottom = MicroPadding,
                            end = MicroPadding
                        ),
                    shape = RoundedCornerShape(7.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state == "FEMALE")
                            AccentColor else LightGrayColor,
                        contentColor = if (state == "FEMALE")
                            Color.White else ChipColor
                    )
                ) {
                    Text(woman)
                }
            }
        }
    }
}

@Preview
@Composable
fun GenderPreview(){
    GenderSelectionButton(updateRole = { }, state = "Male", modifier = Modifier)
}