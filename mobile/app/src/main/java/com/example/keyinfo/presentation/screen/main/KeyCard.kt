package com.example.keyinfo.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
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
import com.example.keyinfo.domain.model.schedule.AudienceStatus
import com.example.keyinfo.presentation.screen.components.customShimmer
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.GreyColor
import com.example.keyinfo.ui.theme.LightBlueColor
import com.example.keyinfo.ui.theme.LightGrayColor
import com.gigamole.composeshadowsplus.softlayer.softLayerShadow
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun KeyCard(
    modifier: Modifier = Modifier,
    audience: Int = 220,
    building: Int = 2,
    startDate: OffsetDateTime = OffsetDateTime.now(),
    endDate: OffsetDateTime = OffsetDateTime.now().plusHours(1).plusMinutes(35),
    status: AudienceStatus = AudienceStatus.FREE,
    onClick: () -> Unit = {},
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .clickable(enabled = onClick != {} && status == AudienceStatus.FREE, onClick = onClick)
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
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.card_audience) + " $audience",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700),
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                if (status == AudienceStatus.OCCUPIED) {
                    Icon(
                        painter = painterResource(id = R.drawable.lock),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AccentColor
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = stringResource(id = R.string.card_building) + " $building",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                ),
                color = LightBlueColor
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
                    text = formatOffsetDateTime(startDate),
                    style = TextStyle(
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
                    text = "${formatOffsetDateTimeTime(startDate)} - ${
                        formatOffsetDateTimeTime(endDate)
                    }",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        fontFamily = FontFamily(Font(R.font.poppins_regular))
                    ), color = LightBlueColor
                )
            }
        }
    }
}

@Composable
fun KeyCardShimmer() {
    Box(Modifier.customShimmer(1000)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GreyColor, shape = RoundedCornerShape(12.dp))
                .height(140.dp)
        ) {
        }
    }
}

fun formatOffsetDateTime(dateTime: OffsetDateTime): String {
    val dayOfWeek = dateTime.dayOfWeek.getDisplayName(
        java.time.format.TextStyle.SHORT,
        Locale.getDefault()
    )
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val dayOfMonth = dateTime.dayOfMonth
    val month = dateTime.month.getDisplayName(
        java.time.format.TextStyle.FULL,
        Locale.getDefault()
    )
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    return "$dayOfWeek, $dayOfMonth $month"
}

fun formatOffsetDateTimeTime(dateTime: OffsetDateTime): String {
    return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
}

//@Preview
//@Composable
//fun KeyCardPreview() {
//    Column(
//        modifier = Modifier
//            .padding(16.dp),
//        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
//    ) {
//        KeyCard(status = AudienceStatus.OCCUPIED)
//        KeyCard(status = AudienceStatus.FREE)
//        KeyCard(status = AudienceStatus.OCCUPIED)
//    }
//}

@Preview
@Composable
fun ShimmerCardPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        KeyCardShimmer()
        KeyCardShimmer()
        KeyCardShimmer()
    }
}