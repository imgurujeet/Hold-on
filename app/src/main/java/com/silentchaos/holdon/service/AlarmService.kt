package com.silentchaos.holdon.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.silentchaos.holdon.R
import com.silentchaos.holdon.utils.SharedPreferencesHelper

class AlarmService : Service() {

    private lateinit var chargerReceiver: BroadcastReceiver
    private var isAlarmActive = false
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()

        // Observe charging/unplug
        chargerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when(intent?.action) {
                    Intent.ACTION_POWER_DISCONNECTED -> triggerAlarm()
                    Intent.ACTION_POWER_CONNECTED -> stopAlarm()
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_POWER_CONNECTED)
        }
        registerReceiver(chargerReceiver, filter)
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "HoldOnServiceChannel"
        val channelName = "Hold On Service"

        val chan = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH // high to play sound
        ).apply {
            setSound(
                null, // handled by MediaPlayer
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(chan)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Hold on")
            .setContentText("Running in the background")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setOngoing(true) // makes it sticky
            .build()

        startForeground(1, notification)
    }

    private fun triggerAlarm() {
        if (!isAlarmActive) {
            isAlarmActive = true
            val soundResId = SharedPreferencesHelper.getAlarmSound(this)
            mediaPlayer = MediaPlayer.create(this, soundResId).apply {
                isLooping = true
                start()
            }
        }
    }

    fun stopAlarm() {
        isAlarmActive = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        unregisterReceiver(chargerReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
