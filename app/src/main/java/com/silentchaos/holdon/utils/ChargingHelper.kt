package com.silentchaos.holdon.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.silentchaos.holdon.receiver.ChargingReceiver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun rememberChargingStatus(): Boolean {
    val context = LocalContext.current
    var isCharging by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val statusIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val status = statusIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }

    // Register receiver once and clean up automatically
    DisposableEffect(Unit) {
        val receiver = ChargingReceiver { charging ->
            isCharging = charging
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        context.registerReceiver(receiver, filter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    return isCharging
}
