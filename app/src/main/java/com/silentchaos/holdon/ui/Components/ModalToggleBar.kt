package com.silentchaos.holdon.ui.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentchaos.holdon.ui.ProtectionModeUI
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ModeToggleBar(
    selectedMode: ProtectionModeUI,
    onModeSelected: (ProtectionModeUI) -> Unit,
    isChargerEnabled: Boolean,
    isPickPocketEnabled: Boolean
) {

    var showInfo by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var hideJob by remember { mutableStateOf<Job?>(null) }

    fun triggerInfo() {
        showInfo = true

        // Cancel previous hide timer
        hideJob?.cancel()
        hideJob = scope.launch {
            delay(2000)
            showInfo = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "MODES",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        AnimatedVisibility(
            visible = showInfo,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Stop protection to switch mode",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        val containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

        Box(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(0.75f)
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(6.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SegmentedItem(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (isChargerEnabled) 1f else 0.4f),
                    text = "Charging",
                    selected = selectedMode == ProtectionModeUI.CHARGER,
                    onClick = {
                        if (isChargerEnabled) {
                            onModeSelected(ProtectionModeUI.CHARGER)
                        } else {
                            triggerInfo()
                        }
                    }
                )

                SegmentedItem(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (isPickPocketEnabled) 1f else 0.4f),
                    text = "Pickpocket",
                    showBeta = true,
                    selected = selectedMode == ProtectionModeUI.PICKPOCKET,
                    onClick = {
                        if (isPickPocketEnabled) {
                            onModeSelected(ProtectionModeUI.PICKPOCKET)
                        } else {
                            triggerInfo()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SegmentedItem(
    modifier: Modifier = Modifier,
    text: String,
    showBeta: Boolean = false,
    selected: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.primary
        else
            Color.Transparent,
        label = ""
    )

    val contentColor by animateColorAsState(
        if (selected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() },
            //.padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor
        )
        if (showBeta) {
            Text(
                text = "Beta",
                fontSize = 8.sp,
                color = contentColor.copy(alpha = 0.5f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 5.dp, end = 20.dp)
            )
        }
    }
}
