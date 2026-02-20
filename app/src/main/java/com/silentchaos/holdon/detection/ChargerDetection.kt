package com.silentchaos.holdon.detection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class ChargerDetection(
    private val context: Context,
    private val onChargerRemoved: () -> Unit,
    private val onChargerConnected: () -> Unit
) {

    private var isRegistered = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_POWER_DISCONNECTED -> onChargerRemoved()
                Intent.ACTION_POWER_CONNECTED -> onChargerConnected()
            }
        }
    }

    fun start() {
        if (isRegistered) return

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_POWER_CONNECTED)
        }

        context.registerReceiver(receiver, filter)
        isRegistered = true
    }

    fun stop() {
        if (!isRegistered) return

        context.unregisterReceiver(receiver)
        isRegistered = false
    }
}
