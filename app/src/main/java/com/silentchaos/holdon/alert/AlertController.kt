package com.silentchaos.holdon.alert

import android.content.Context
import com.silentchaos.holdon.data.preferences.AppPreferences
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.engine.AlertType
import com.silentchaos.holdon.engine.SecurityEngine
import com.silentchaos.holdon.engine.SecurityState
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

    fun showVerification(type: AlertType) {
        notificationManager.show(type) // full-screen notification only
    }

    fun startAlert(type: AlertType) {

        if (SecurityEngine.state.value is SecurityState.Alert) {
            // Already alerting, don't start again
        }


        scope.launch {

            val soundResId = preferences.alarmSound.first()

            audioManager.stop() //  IMPORTANT
            audioManager.start(soundResId)
        }
    }

    fun stopAlert() {
        audioManager.stop()
        notificationManager.cancel()
    }
}
