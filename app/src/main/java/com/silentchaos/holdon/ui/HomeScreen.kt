package com.silentchaos.holdon.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.silentchaos.holdon.appNavigation.Route
import com.silentchaos.holdon.utils.isDeviceCharging
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.silentchaos.holdon.receiver.ChargingReceiver


@Composable
fun HomeScreen(navController: NavController,context: Context) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    var isCharging by remember { mutableStateOf(isDeviceCharging(context)) }

    val receiver = remember { ChargingReceiver { isCharging = it } }

    // Register receiver only while this Composable is active
    DisposableEffect(Unit) {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
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
                    .windowInsetsPadding(WindowInsets.navigationBars) // Push above nav bar
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = { },
                    enabled = isCharging,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight.dp * 0.05f)
                ) {
                    Text(
                        text = "Hold On",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Thin),
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
                       if(isCharging) R.drawable.smartphone_charging else R.drawable.smartphone
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




@Composable
fun TopBar(navController: NavController, screenWidth: Int, screenHeight: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.systemBars)

    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "info Icon",
            modifier = Modifier
                .width(screenWidth.dp * 0.05f)
                .height(screenHeight.dp * 0.05f)
                .align (Alignment.CenterStart)
                .clickable(
                    onClick = { navController.navigate(Route.Info.route) } // Replace with your navigation logic
                ),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

        Icon(
            imageVector =Icons.Default.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier
                .width(screenWidth.dp * 0.05f)
                .height(screenHeight.dp * 0.05f)
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = { navController.navigate(Route.Setting.route) } // Replace with your navigation logic
                ),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

