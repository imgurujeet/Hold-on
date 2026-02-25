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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silentchaos.holdon.R
import com.silentchaos.holdon.biometric.Authenticator
import com.silentchaos.holdon.engine.SecurityEngineService
import com.silentchaos.holdon.ui.Components.AlarmVolumeSection
import com.silentchaos.holdon.ui.Components.ChargingStatusSection
import com.silentchaos.holdon.ui.Components.ModeToggleBar
import com.silentchaos.holdon.ui.Components.PickPocketInfoCard
import com.silentchaos.holdon.ui.Components.PickPocketStatusSection
import com.silentchaos.holdon.ui.Components.TopBar
import com.silentchaos.holdon.ui.viewmodel.HomeViewModel

enum class ProtectionModeUI {
    CHARGER,
    PICKPOCKET
}


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

    val selectedMode = uiState.selectedMode



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopBar(
                onInfoClick = onInfoClick,
                onSettingClick = onSettingClick,
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                centerContent = {

                }
            )

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

                            // START with selected mode
                            val intent = Intent(
                                context,
                                SecurityEngineService::class.java
                            ).apply {
                                action = SecurityEngineService.ACTION_START
                                putExtra(
                                    SecurityEngineService.EXTRA_MODE,
                                    when (uiState.selectedMode) {
                                        ProtectionModeUI.CHARGER ->
                                            SecurityEngineService.MODE_CHARGER

                                        ProtectionModeUI.PICKPOCKET ->
                                            SecurityEngineService.MODE_PICKPOCKET
                                    }
                                )
                            }

                            ContextCompat.startForegroundService(context, intent)

                        } else {

                            // STOP with authentication
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
                                            "Protection disabled",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ){
                ModeToggleBar(
                    selectedMode = selectedMode,
                    onModeSelected = { mode ->
                        if (!uiState.isMonitoring) {
                            viewModel.setMode(mode)
                        }
                    },
                    isChargerEnabled =
                        !uiState.isMonitoring || selectedMode == ProtectionModeUI.CHARGER,

                    isPickPocketEnabled =
                        !uiState.isMonitoring || selectedMode == ProtectionModeUI.PICKPOCKET
                )
            }

//            if (selectedMode == ProtectionModeUI.PICKPOCKET) {
//                PickPocketInfoCard()
//            }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {




            when (selectedMode) {

                ProtectionModeUI.CHARGER -> {
                    ChargingStatusSection(
                        isCharging = uiState.isCharging,
                        batteryPercent = uiState.batteryPercent,
                        color = when {
                            uiState.batteryPercent < 20 ->
                                MaterialTheme.colorScheme.error
                            uiState.batteryPercent > 80 ->
                                MaterialTheme.colorScheme.primary
                            else ->
                                MaterialTheme.colorScheme.tertiary
                        },
                    )
                }

                ProtectionModeUI.PICKPOCKET -> {
                    PickPocketStatusSection(
                        isMonitoring = uiState.isMonitoring
                    )
                }
            }


            Spacer(Modifier.height(20.dp))


            Spacer(Modifier.height(40.dp))

            Spacer(Modifier.height(50.dp))

            AlarmVolumeSection(
                volumePercent = uiState.volumePercent,
                onVolumeChange = { viewModel.setAlarmVolumeFromPercent(it) }
            )
        }
        }
    }
}

