package com.silentchaos.holdon.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.silentchaos.holdon.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.Manifest
import android.content.pm.PackageManager
import com.silentchaos.holdon.service.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d("AlarmReceiver", "Charger unplugged detected!")
                try {
                    val serviceIntent = Intent(context, AlarmService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                } catch (e: Exception) {
                    Log.e("AlarmReceiver", "Failed to start service: ${e.message}")
                }
            }
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d("AlarmReceiver", "Charger plugged in!")
                try {
                    context.stopService(Intent(context, AlarmService::class.java))
                } catch (e: Exception) {
                    Log.e("AlarmReceiver", "Failed to stop service: ${e.message}")
                }
            }
        }
    }
}
