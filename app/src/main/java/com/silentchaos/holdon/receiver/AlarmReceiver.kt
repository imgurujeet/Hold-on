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
                    val serviceIntent = Intent(context, AlarmService::class.java)
                context.startForegroundService(serviceIntent)

            }
            Intent.ACTION_POWER_CONNECTED -> {
                    context.stopService(Intent(context, AlarmService::class.java))


            }
        }
    }
}
