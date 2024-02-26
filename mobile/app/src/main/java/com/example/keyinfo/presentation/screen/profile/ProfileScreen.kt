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
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    clickOnLeave: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

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

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
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


@Preview()
@Composable
fun BtmPreview() {
    BottomSheetContent({}, {})
}
