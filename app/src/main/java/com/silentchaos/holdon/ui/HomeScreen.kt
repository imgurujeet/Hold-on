package com.silentchaos.holdon.ui

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silentchaos.holdon.R
import com.silentchaos.holdon.biometric.Authenticator
import com.silentchaos.holdon.engine.SecurityEngineService
import com.silentchaos.holdon.ui.Components.TopBar
import com.silentchaos.holdon.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    onInfoClick: () -> Unit,
    onSettingClick: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val authenticator = remember { Authenticator() }


    val isCharging = uiState.isCharging

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopBar(onInfoClick, onSettingClick, screenWidth, screenHeight)
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = {

                        if (!uiState.isMonitoring) {

                            //  START MONITORING
                            val intent = Intent(
                                context,
                                SecurityEngineService::class.java
                            ).apply {
                                action = SecurityEngineService.ACTION_START
                            }

                            ContextCompat.startForegroundService(context, intent)

                        } else {

                            // STOP WITH AUTHENTICATION
                            activity?.let { act ->
                                authenticator.authenticate(
                                    activity = act,
                                    onSuccess = {

                                        val stopIntent = Intent(
                                            context,
                                            SecurityEngineService::class.java
                                        ).apply {
                                            action = SecurityEngineService.ACTION_STOP
                                        }

                                        ContextCompat.startForegroundService(context, stopIntent)


                                        Toast.makeText(
                                            context,
                                            "Monitoring stopped",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    },
                    enabled = uiState.buttonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight.dp * 0.05f)
                ) {
                    Text(
                        text = uiState.buttonText,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Thin
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {

                if (isCharging) {
                    CircularWavyProgressIndicator(
                        progress = { uiState.batteryPercent / 100f },
                        amplitude = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = when {
                            uiState.batteryPercent < 20 ->
                                MaterialTheme.colorScheme.error
                            uiState.batteryPercent > 80 ->
                                MaterialTheme.colorScheme.primary
                            else ->
                                MaterialTheme.colorScheme.tertiary
                        },
                        trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                        gapSize = 20.dp,
                        wavelength = 80.dp,
                        stroke = Stroke(
                            width = 28f,   //  thickness in pixels
                            cap = StrokeCap.Round
                        ),
                        waveSpeed = 40.dp
                    )


            }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(
                            id = if (isCharging)
                                R.drawable.smartphone_charging
                            else
                                R.drawable.smartphone
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(8.dp))

                    if (isCharging) {
                        Text(
                            text = "  ${uiState.batteryPercent}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }



            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = uiState.chargingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            Spacer(Modifier.height(50.dp))

            if (uiState.isCharging) {

                val isLow = uiState.volumePercent < 50

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLow)
                                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.18f)
                            else
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isLow)
                                MaterialTheme.colorScheme.error.copy(alpha = 0.25f)
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .fillMaxWidth(0.9f)
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = if (isLow)
                                    "Alarm volume is low"
                                else
                                    "Alarm volume looks good",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isLow)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isLow)
                                    "You might not hear the alert clearly."
                                else
                                    "You're good to go. You can adjust it anytime.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${uiState.volumePercent}%",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Slider(
                                value = uiState.volumePercent.toFloat(),
                                onValueChange = { viewModel.setAlarmVolumeFromPercent(it) },
                                valueRange = 0f..100f,
                                modifier = Modifier.fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = if (isLow)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary,
                                    activeTrackColor = if (isLow)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
