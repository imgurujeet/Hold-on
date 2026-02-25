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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.silentchaos.holdon.R


@Composable
fun PickPocketStatusSection(
    isMonitoring: Boolean
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            painter = painterResource(
                id = if (isMonitoring)
                    R.drawable.ic_watching
                else
                    R.drawable.ic_pickpocket
            ),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = if (isMonitoring)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(25.dp))

        Text(
            text = if (isMonitoring)
                "Pickpocket Protection Active"
            else
                "Pickpocket Protection Inactive",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Detects suspicious movement while device is locked.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}