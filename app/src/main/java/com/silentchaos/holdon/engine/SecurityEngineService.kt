package com.silentchaos.holdon.engine

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.silentchaos.holdon.alert.AlertAudioManager
import com.silentchaos.holdon.alert.AlertController
import com.silentchaos.holdon.alert.AlertNotificationManager
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.detection.ChargerDetection
import com.silentchaos.holdon.detection.PickPocketConfig
import com.silentchaos.holdon.detection.PickPocketDetection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SecurityEngineService : Service() {

    companion object {
        private const val CHANNEL_ID = "security_engine_channel"
        private const val NOTIFICATION_ID = 1

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        const val EXTRA_MODE = "EXTRA_MODE"
        const val MODE_CHARGER = "MODE_CHARGER"
        const val MODE_PICKPOCKET = "MODE_PICKPOCKET"
    }

    private lateinit var chargerDetection: ChargerDetection
    private var pickPocketDetection: PickPocketDetection? = null
    private lateinit var alertController: AlertController
    private lateinit var preferences: AppPreferencesImpl

    private var isEngineRunning = false
    private var currentMode: String = MODE_CHARGER

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
                currentMode = intent.getStringExtra(EXTRA_MODE)
                    ?: MODE_CHARGER
                startEngine()
            }

            ACTION_STOP -> {
                stopEngine()
                stopSelf()
            }

            null -> {
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

        when (currentMode) {

            MODE_CHARGER -> {
                chargerDetection.start()
                SecurityEngine.startMonitoring()
            }

            MODE_PICKPOCKET -> {

                if (isDeviceCharging()) {
                    isEngineRunning = false
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    return
                }

                // Read latest config from DataStore
                val config = runBlocking {
                    PickPocketConfig(
                        motionThreshold = preferences.motionThreshold.first(),
                        verificationDelay = preferences.verificationDelay.first()
                    )
                }

                SecurityEngine.updatePickPocketVerificationDelay(config.verificationDelay)
                pickPocketDetection = PickPocketDetection(
                    context = this,
                    config = config,
                    onSuspiciousMovement = {
                        SecurityEngine.onPickPocketDetected()
                    }
                )

                // Disable feature if no accelerometer
                if (pickPocketDetection?.hasAccelerometer == false) {
                    isEngineRunning = false
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    return
                }

                SecurityEngine.startMonitoring()
                pickPocketDetection?.start()
            }
        }
    }

    private fun stopEngine() {

        if (!isEngineRunning) return
        isEngineRunning = false

        chargerDetection.stop()
        pickPocketDetection?.stop()

        SecurityEngine.stopMonitoring()

        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onDestroy() {

        if (isEngineRunning) {
            chargerDetection.stop()
            pickPocketDetection?.stop()
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

        val text = if (currentMode == MODE_PICKPOCKET)
            "Monitoring pickpocket protection"
        else
            "Monitoring charger status"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("HoldOn Active")
            .setContentText(text)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun isDeviceCharging(): Boolean {

        val batteryStatus = registerReceiver(
            null,
            android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val status = batteryStatus?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            -1
        )

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }
}