package com.example.keyinfo.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.LightBlueColor
import com.example.keyinfo.ui.theme.LightGrayColor
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow

@Composable
fun KeyCard() {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.softLayerShadow(
            spread = 1.dp,
            color = Color.Black.copy(alpha = 0.02f),
            radius = 2.dp,
            offset = DpOffset(0.dp, 5.dp),
            shape = RoundedCornerShape(12.dp),
            isAlphaContentClip = true
        )

    ) {
        Column(
            Modifier.padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.card_audience),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                ),
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = stringResource(id = R.string.card_building), style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                ), color = LightBlueColor
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = LightGrayColor
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = LightBlueColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.card_date), style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    ), color = LightBlueColor
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = LightBlueColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.card_time), style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    ), color = LightBlueColor
                )
            }

        }
    }
}

@Preview
@Composable
fun KeyCardPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        KeyCard()
        KeyCard()
        KeyCard()
    }
}