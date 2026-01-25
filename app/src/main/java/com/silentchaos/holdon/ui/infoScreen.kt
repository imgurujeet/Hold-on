package com.silentchaos.holdon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InfoScreen(navController: NavController) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)
            ){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon",
                    modifier = Modifier
                        .width(screenWidth.dp * 0.08f)
                        .height(screenHeight.dp * 0.08f)
                        .padding(end = 8.dp)
                        .clickable(
                            onClick = { navController.popBackStack() } // Replace with your navigation logic
                        ),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                Text(
                    text = "Info",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        },
       content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = "Why Hold On?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Thin),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "In train, cafés, airport, or crowded places, your phone is at risk. Hold On acts as your phone’s security guard — if someone unplugs it, a loud alarm goes off instantly to deter theft",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                    )

                Text(
                    text = "How It Works",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Thin),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Hold On monitors your charging status. If unplugged without permission, it triggers a high-decibel alert.\n" +
                            "The alarm stops only when you unlock your phone (PIN, password, pattern, or biometrics) or reconnect the charger (if enabled)." ,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Quick Start",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Thin),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = " 1. Plug in your phone.\n" + " 2. Open Hold On → Tap Activate Protection.\n" + " 3. Lock your phone and walk away confidently.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Disarming",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Thin),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = " 1. Tap Disarm → Authenticate.\n"+ " 2. Or reconnect the charger.\n ",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )


            }
        }
    )

}