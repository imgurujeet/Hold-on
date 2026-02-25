package com.silentchaos.holdon.ui.Components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.silentchaos.holdon.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ChargingStatusSection(
    isCharging: Boolean,
    batteryPercent: Int,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp)
        ) {

            if (isCharging) {
                CircularWavyProgressIndicator(
                    progress = { batteryPercent / 100f },
                    amplitude = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = color,
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                    gapSize = 20.dp,
                    wavelength = 80.dp,
                    stroke = Stroke(width = 28f, cap = StrokeCap.Round),
                    waveSpeed = 40.dp
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Icon(
                    painter = painterResource(
                        if (isCharging)
                            R.drawable.smartphone_charging
                        else
                            R.drawable.smartphone
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isCharging) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$batteryPercent%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = if (isCharging)
                "Charging"
            else
                "Plug in your charger",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal
        )





    }

}