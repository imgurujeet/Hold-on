package com.silentchaos.holdon.alert

import android.content.Context
import com.silentchaos.holdon.data.preferences.AppPreferences
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.engine.AlertType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertController(
    private val audioManager: AlertAudioManager,
    private val notificationManager: AlertNotificationManager,
    private val preferences: AppPreferences
) {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun startAlert(type: AlertType) {

        scope.launch {

            // Get selected sound from DataStore (only once)
            val soundResId = preferences.alarmSound.first()

            audioManager.start(soundResId)
            notificationManager.show(type)
        }
    }

    fun stopAlert() {
        audioManager.stop()
        notificationManager.cancel()
    }
}
