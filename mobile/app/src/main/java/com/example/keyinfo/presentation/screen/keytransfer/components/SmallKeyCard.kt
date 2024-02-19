package com.example.keyinfo.presentation.screen.keytransfer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.LightBlueColor
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow

@Composable
fun SmallKeyCard(
    title: String = "220",
    description: String = "2",
    buttonState: Int = 0,
    name: String? = null,
    isAudience: Boolean = true
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .softLayerShadow(
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
                text = if (isAudience) stringResource(id = R.string.card_audience) + " $title" else title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                ),
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = if (isAudience) stringResource(id = R.string.card_building) + " $description" else description,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                ),
                color = LightBlueColor
            )
            if (name != null) {
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Text(
                    text = if (buttonState == 0) "От: $name" else "Кому: $name",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                    ),
                    color = LightBlueColor
                )
            }

        }
    }
}

@Preview
@Composable
fun SmallKeyCardPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        SmallKeyCard()
        SmallKeyCard()
        SmallKeyCard()
    }
}
