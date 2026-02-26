package com.silentchaos.holdon.ui.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PickPocketModeSheet(
    currentMode: String,
    motionThreshold: Float,
    verificationDelay: Long,
    onModeSelected: (String) -> Unit,
    onThresholdChange: (Float) -> Unit,
    onDelayChange: (Long) -> Unit
){
    var selectedMode by remember(currentMode) {
        mutableStateOf(currentMode)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {

        Text(
            text = "Pickpocket Sensitivity",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(24.dp))

        ModeOption(
            title = "Normal",
            description = "Balanced protection for everyday use.",
            selected = selectedMode == "normal",
            onClick = {
                selectedMode = "normal"
                onModeSelected("normal")
            }
        )

        ModeOption(
            title = "Crowded",
            description = "Higher sensitivity for public transport.",
            selected = selectedMode == "crowded",
            onClick = {
                selectedMode = "crowded"
                onModeSelected("crowded")
            }
        )

        ModeOption(
            title = "High Security",
            description = "Maximum detection. May trigger more easily.",
            selected = selectedMode == "high",
            onClick = {
                selectedMode = "high"
                onModeSelected("high")
            }
        )

        ModeOption(
            title = "Custom",
            description = "Adjust motion and delay manually.",
            selected = selectedMode == "custom",
            onClick = {
                selectedMode = "custom"
                onModeSelected("custom")
            }
        )

        if (selectedMode == "custom") {

            Spacer(Modifier.height(24.dp))

            // ---------------- MOTION THRESHOLD ----------------

            Text(
                text = "Motion Sensitivity (${motionThreshold.format(1)})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Slider(
                value = motionThreshold,
                onValueChange = onThresholdChange,
                valueRange = 2f..25f
            )

            Text(
                text = when {
                    motionThreshold < 5f ->
                        "Very sensitive. Even small movements may trigger detection."
                    motionThreshold < 12f ->
                        "Balanced sensitivity. Suitable for normal usage."
                    else ->
                        "Low sensitivity. Requires strong motion to trigger."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(20.dp))

            // ---------------- VERIFICATION DELAY ----------------

            val delaySeconds = (verificationDelay / 1000).toInt()

            Text(
                text = "Verification Delay ($delaySeconds sec)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Slider(
                value = delaySeconds.toFloat(),
                onValueChange = { seconds ->
                    onDelayChange((seconds.toInt() * 1000L))
                },
                valueRange = 1f..10f,
                steps = 8   // (10 - 1 - 1) = 8 intermediate steps
            )

            Text(when {
                delaySeconds <= 2 ->
                    "Short delay. Alarm triggers quickly after movement."
                delaySeconds <= 5 ->
                    "Moderate delay. Gives time to unlock normally."
                else ->
                    "Long delay. Extra time before alarm triggers."
            },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            // ---------------- CUMULATIVE BEHAVIOR CARD ----------------

            val summaryColor =
                if (motionThreshold < 6f && verificationDelay < 1500)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = summaryColor.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Current Behaviour",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "If strong motion is detected, you will have ${(verificationDelay / 1000)} seconds to unlock the device. If not verified, the alarm will activate.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}


@Composable
fun ModeOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = if (selected) 4.dp else 0.dp,
        border = if (selected)
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        else null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun Float.format(digits: Int) =
    "%.${digits}f".format(this)