package com.silentchaos.holdon.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ChargingReceiver(private val onCharging: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                onCharging(true)
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                onCharging(false)
            }
        }
    }
}