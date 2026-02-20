package com.silentchaos.holdon.engine

import com.silentchaos.holdon.alert.AlertController
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.silentchaos.holdon.R
import com.silentchaos.holdon.alert.AlertAudioManager
import com.silentchaos.holdon.alert.AlertNotificationManager
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.detection.ChargerDetection

class SecurityEngineService : Service() {

    companion object {
        private const val CHANNEL_ID = "security_engine_channel"
        private const val NOTIFICATION_ID = 1

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private lateinit var chargerDetection: ChargerDetection
    private lateinit var alertController: AlertController
    private var isEngineRunning = false
    private lateinit var preferences: AppPreferencesImpl

    override fun onCreate() {
        super.onCreate()
        preferences = AppPreferencesImpl(applicationContext)
        val audioManager = AlertAudioManager(this)
        val notificationManager = AlertNotificationManager(this)

        alertController = AlertController(
            audioManager = audioManager,
            notificationManager = notificationManager,
            preferences = preferences
        )
        SecurityEngine.initialize(alertController)

        chargerDetection = ChargerDetection(
            context = this,
            onChargerRemoved = { SecurityEngine.onChargerRemoved() },
            onChargerConnected = { SecurityEngine.onChargerConnected() }
        )
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        when (intent?.action) {

            ACTION_START -> {
                startEngine()
            }

            ACTION_STOP -> {
                stopEngine()
                stopSelf()
            }

            null -> {
                // If system restarts service
                startEngine()
            }
        }

        return START_STICKY
    }


    private fun startEngine() {

        if (isEngineRunning) return
        isEngineRunning = true

        startForeground(
            NOTIFICATION_ID,
            createForegroundNotification()
        )

        chargerDetection.start()
        SecurityEngine.startMonitoring()
    }

    private fun stopEngine() {

        if (!isEngineRunning) return
        isEngineRunning = false

        chargerDetection.stop()
        SecurityEngine.stopMonitoring()

        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onDestroy() {

        if (isEngineRunning) {
            chargerDetection.stop()
            SecurityEngine.stopMonitoring()
        }

        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder? = null

    private fun createForegroundNotification(): Notification {

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Security Monitoring",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("HoldOn Active")
            .setContentText("Monitoring charger status")
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
}
