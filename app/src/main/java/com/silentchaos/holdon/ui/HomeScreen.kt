package com.silentchaos.holdon.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silentchaos.holdon.R
import androidx.compose.ui.text.font.FontWeight
import com.silentchaos.holdon.utils.rememberChargingStatus
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.service.AlarmService
import com.silentchaos.holdon.ui.Components.TopBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import authenticateAndStopService
import com.silentchaos.holdon.ui.viewmodel.HomeViewModel


@Composable
fun HomeScreen(navController: NavController) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    val isCharging = rememberChargingStatus()
    var isObserving by remember { mutableStateOf(false) }

    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Update VM when charging or observing changes
    LaunchedEffect(isCharging, isObserving) {
        viewModel.updateState(isCharging, isObserving)
    }

    // Auto stop when unplugged
    LaunchedEffect(isCharging) {
        if (uiState.isObserving && !isCharging) {
            activity?.let {
                authenticateAndStopService(it) {
                    context.stopService(Intent(context, AlarmService::class.java))
                    isObserving = false
                    Toast.makeText(context, "Service stopped - Charger unplugged", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopBar(navController, screenWidth, screenHeight)
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
                        val serviceIntent = Intent(context, AlarmService::class.java)

                        if (!isObserving) {
                            ContextCompat.startForegroundService(context, serviceIntent)
                            isObserving = true
                        } else {
                            activity?.let {
                                authenticateAndStopService(it) {
                                    context.stopService(serviceIntent)
                                    isObserving = false
                                }
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

            Icon(
                painter = painterResource(
                    id = if (uiState.isCharging)
                        R.drawable.smartphone_charging
                    else
                        R.drawable.smartphone
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(screenWidth.dp * 0.2f)
                    .height(screenHeight.dp * 0.2f),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Text(
                text = uiState.chargingText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Thin),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            )


            Spacer(Modifier.height(50.dp ))

            if (uiState.isVolumeLow && uiState.isCharging) {

                Box(
                    modifier = Modifier.background(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
                    ).border(
                        border = BorderStroke(
                            width = 1.dp , MaterialTheme.colorScheme.primary.copy(0.5f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Volume is below 50%. \n" +
                                "Raise it for a louder alarm.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
