package com.silentchaos.holdon.alert

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.R
import com.silentchaos.holdon.engine.AlertType
import com.silentchaos.holdon.ui.alarmAlert.AlarmAlertActivity

class AlertNotificationManager(
    private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "alert_channel"
        private const val NOTIFICATION_ID = 1001
    }

    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun show(type: AlertType) {

        createChannel()

        val fullScreenIntent = Intent(context, AlarmAlertActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(context, AlarmAlertActivity::class.java).apply {
            action = "AUTH_FROM_NOTIFICATION"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val stopPendingIntent = PendingIntent.getActivity(
            context,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("🚨 Charger Disconnected")
            .setContentText("Authenticate to stop the alert")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true) //
            .setColor(ContextCompat.getColor(context, R.color.alarm_red))
            .setColorized(true)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_lock_power_off,
                "STOP",
                stopPendingIntent
            )
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }


    fun cancel() {
        manager.cancel(NOTIFICATION_ID)
    }

    private fun createChannel() {
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Security Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)
    }
}
