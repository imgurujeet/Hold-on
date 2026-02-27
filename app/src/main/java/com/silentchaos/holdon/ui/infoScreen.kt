package com.silentchaos.holdon.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun InfoScreen(
    onBackClick: () -> Unit
) {

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
                            onClick = { onBackClick() } // Replace with your navigation logic
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
                   .verticalScroll(rememberScrollState())
                   .padding(horizontal = 12.dp),
           ) {

               // -------------------------------------------------
               // WHY HOLD ON
               // -------------------------------------------------

               Text(
                   text = "Why Hold On?",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "In trains, cafés, airports, hostels, offices, and other public environments, smartphones are highly vulnerable to theft. " +
                           "Hold On acts as a real-time physical security layer for your device — immediately reacting to unauthorized actions.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // CHARGER PROTECTION
               // -------------------------------------------------

               Text(
                   text = "Charger Protection",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "When activated, Hold On monitors your charging state. " +
                           "If the charger is disconnected without authorization, a high-decibel alarm is triggered instantly to deter theft.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "The alarm stops only after successful authentication (PIN, pattern, password, or biometrics), " +
                           "or by reconnecting the charger.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // PICKPOCKET PROTECTION
               // -------------------------------------------------

               Text(
                   text = "Pickpocket Protection",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "Pickpocket Mode uses motion-based detection to monitor suspicious device movement. " +
                           "If your phone is forcefully removed from a pocket, bag, or resting surface, Hold On detects abnormal motion patterns.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "If strong motion is detected, you are given a short verification window to unlock your device. " +
                           "If not verified within the configured delay, the alarm activates automatically.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // SENSITIVITY MODES
               // -------------------------------------------------

               Text(
                   text = "Sensitivity Modes",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text =
                       "• Normal – Balanced detection for everyday use.\n\n" +
                               "• Crowded – Higher sensitivity for buses, metros, events, and public gatherings.\n\n" +
                               "• High Security – Maximum detection sensitivity. Recommended for high-risk environments.\n\n" +
                               "• Custom – Manually configure motion sensitivity and verification delay for advanced control.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // PROFESSIONAL USAGE GUIDE
               // -------------------------------------------------

               Text(
                   text = "Professional Usage Guide",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text =
                       "For public travel: Use Crowded or High Security mode.\n\n" +
                               "For cafés or shared workspaces: Enable Charger Protection before stepping away.\n\n" +
                               "For corporate or field environments: Use Pickpocket Mode with a moderate verification delay to prevent accidental triggers.\n\n" +
                               "For maximum safety: Combine Charger Protection and Pickpocket Protection based on your scenario.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // QUICK START
               // -------------------------------------------------

               Text(
                   text = "Quick Start",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text =
                       "1. Select your protection mode.\n" +
                               "2. Tap Activate.\n" +
                               "3. Lock your device.\n" +
                               "4. Leave with confidence.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 16.dp)
               )

               // -------------------------------------------------
               // DISARMING
               // -------------------------------------------------

               Text(
                   text = "Disarming Protection",
                   style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                   modifier = Modifier.padding(bottom = 8.dp)
               )

               Text(
                   text = "To stop protection, tap Disarm and authenticate (PIN, pattern, password, or biometrics).\n\n" +
                           "You must disarm before switching modes. If authentication is not completed, the alarm will continue.",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.padding(bottom = 24.dp)
               )
           }
        }
    )

}