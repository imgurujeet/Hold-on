package com.silentchaos.holdon.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.silentchaos.holdon.appNavigation.Route
import androidx.compose.ui.text.font.FontWeight
import com.silentchaos.holdon.utils.rememberChargingStatus
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.service.AlarmService
import com.silentchaos.holdon.ui.Components.TopBar


@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val context = LocalContext.current
    val isCharging = rememberChargingStatus()
    var isObserving by remember { mutableStateOf(false) }


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
                    .windowInsetsPadding(WindowInsets.navigationBars) // Push above nav bar
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
                            // Stop observing
                            context.stopService(serviceIntent)
                            isObserving = false
                            Toast.makeText(context, "Stopped Observing", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = isCharging, // You can still allow stopping even if not charging
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight.dp * 0.05f)
                ) {
                    Text(
                        text = if (isObserving) "Stop Holding" else "Hold On",
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
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
            }
        }
    )
}




