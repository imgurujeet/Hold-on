package com.silentchaos.holdon.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.service.AlarmService
import com.silentchaos.holdon.ui.Components.TopBar
import androidx.fragment.app.FragmentActivity
import authenticateAndStopService


@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val isCharging = rememberChargingStatus()
    var isObserving by remember { mutableStateOf(false) }
    var previousChargingState by remember { mutableStateOf(isCharging) }

    // Auto-authentication when charger is unplugged
    LaunchedEffect(isCharging) {
        // If service is running and charger was plugged but now unplugged
        if (isObserving && previousChargingState && !isCharging) {
            activity?.let { fragmentActivity ->
                authenticateAndStopService(fragmentActivity) {
                    val serviceIntent = Intent(context, AlarmService::class.java)
                    context.stopService(serviceIntent)
                    isObserving = false
                    Toast.makeText(context, "Service stopped - Charger unplugged", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                // Fallback if activity not available
                val serviceIntent = Intent(context, AlarmService::class.java)
                context.stopService(serviceIntent)
                isObserving = false
                Toast.makeText(context, "Service stopped - Charger unplugged", Toast.LENGTH_SHORT).show()
            }
        }
        previousChargingState = isCharging
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
                            // Start observing
                            ContextCompat.startForegroundService(context, serviceIntent)
                            isObserving = true
                            Toast.makeText(context, "Started Observing", Toast.LENGTH_SHORT).show()
                        } else {
                            // Stop observing with authentication (manual stop)
                            activity?.let { fragmentActivity ->
                                authenticateAndStopService(fragmentActivity) {
                                    context.stopService(serviceIntent)
                                    isObserving = false
                                   // Toast.makeText(context, "Service stopped manually", Toast.LENGTH_SHORT).show()
                                }
                            } ?: run {
                                // Fallback if activity casting fails
                                context.stopService(serviceIntent)
                                isObserving = false
                               // Toast.makeText(context, "Service stopped", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = isCharging || isObserving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight.dp * 0.05f)
                ) {
                    Text(
                        text = if (isObserving) "Disarm" else "Hold On",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Thin
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        content = { innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isCharging) R.drawable.smartphone_charging
                        else R.drawable.smartphone
                    ),

                    contentDescription = "App Icon",
                    modifier = Modifier
                        .width(screenWidth.dp * 0.2f)
                        .height(screenHeight.dp * 0.2f),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                Text(
                    text= if (!isCharging) "Plug in your charger" else "Charging",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Thin),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                )
            }
        }
    )
}