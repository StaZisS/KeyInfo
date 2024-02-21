package com.example.keyinfo.presentation.screen.profile

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyinfo.R
import com.example.keyinfo.ui.theme.AccentColor
import com.example.keyinfo.ui.theme.LightBlueColor
import com.example.keyinfo.ui.theme.SecondButtonColor
import com.example.keyinfo.ui.theme.TextButtonColor
import com.example.keyinfo.ui.theme.WhiteColor


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    clickOnLeave: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val vibratorManager =
        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator
    val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    DisposableEffect(state.isShaking) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = kotlin.math.sqrt(x * x + y * y + z * z)

                if (acceleration > 22) {
                    viewModel.showSheet.value = true
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        sensorManager.registerListener(
            sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
        )

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }


    LaunchedEffect(Unit) {
        viewModel.getProfile()
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(112.dp),
            painter = painterResource(id = R.drawable.profile),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(4.dp),
                color = AccentColor,
                trackColor = SecondButtonColor
            )
        } else {
            Text(
                text = state.name,
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight(700),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = state.role,
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular)
                    )
                ),
                color = LightBlueColor
            )


        }
        Spacer(modifier = Modifier.height(5.dp))
        TextButton(
            onClick = { viewModel.logoutUser(clickOnLeave) },
            colors = TextButtonColor,
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Выйти из аккаунта",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
    if (viewModel.showSheet.value) {
        BottomSheet(
            onFirstClicked = {
                viewModel.showSheet.value = false
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://dodopizza.ru/tomsk")
                )
                context.startActivity(intent)
            },
            onSecondClicked = {
                viewModel.showSheet.value = false
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://makelovepizza.ru/tomsk")
                )
                context.startActivity(intent)
            },
            onDismissSheet = {
                viewModel.showSheet.value = false
            }
        )
    }
}

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
        }
    )
}

@Composable
fun BottomSheetContent(
    onFirstClicked: () -> Unit,
    onSecondClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Заказать пиццу",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins)
                    ),
                    textAlign = TextAlign.Center,
                    color = AccentColor
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onFirstClicked()
                    },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6900),
                        disabledContainerColor = Color(0xFFFF6900)
                    ),
                ) {
                    Icon(
                        painterResource(id = R.drawable.pizza),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        tint = WhiteColor
                    )
                    Text(
                        "Додо Пицца", style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        onSecondClicked()
                    },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFCC1B),
                        disabledContainerColor = Color(0xFFFFCC1B)
                    ),
                ) {
                    Icon(
                        painterResource(id = R.drawable.pizza),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp),
                        tint = WhiteColor
                    )
                    Text(
                        "Make Love", style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}


@Preview()
@Composable
fun BtmPreview() {
    BottomSheetContent({}, {})
}
