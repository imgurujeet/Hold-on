package com.silentchaos.holdon.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.silentchaos.holdon.R


@Composable
fun AlarmVolumeSection(
    volumePercent: Int,
    onVolumeChange: (Float) -> Unit
) {

    val isLow = volumePercent < 50
    var showFull by remember { mutableStateOf(true) }

    // Auto hide logic when volume is good
    LaunchedEffect(showFull, isLow) {
        if (showFull && !isLow) {
            kotlinx.coroutines.delay(3000)
            showFull = false
        }
    }

    LaunchedEffect(volumePercent) {
        if (volumePercent < 50) {
            showFull = true
        }
    }

    if (!showFull && !isLow) {
        // Minimal collapsed version
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(0.9f)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 14.dp)
                    .fillMaxWidth()
                    .clickable { showFull = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Alarm Volume · $volumePercent%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary.copy(0.5f)
                )
            }
        }

        return
    }

    // -------- Full Card (your original design) --------

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
            modifier = Modifier.padding(24.dp),
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

            Spacer(Modifier.height(4.dp))

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
                text = "$volumePercent%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Slider(
                value = volumePercent.toFloat(),
                onValueChange = onVolumeChange,
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}