package com.silentchaos.holdon.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.content.edit

data class HomeUiState(
    val isCharging: Boolean = false,
    val isObserving: Boolean = false,
    val isVolumeLow: Boolean = false,
    val buttonEnabled: Boolean = false,
    val buttonText: String = "Hold On",
    val chargingText: String = "Plug in your charger"
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val audioManager =
        application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val prefs =
        application.getSharedPreferences("service_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val volumeObserver = object : ContentObserver(
        Handler(Looper.getMainLooper())
    ) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            updateVolumeOnly()
        }


    }

    init {
        //  Observe only MUSIC volume changes
        application.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            volumeObserver
        )
        val savedObserving = prefs.getBoolean("is_running", false)
        val realCharging = isDeviceCharging()
        updateState(isCharging = realCharging, isObserving = savedObserving)
        updateVolumeOnly()
    }

    fun updateState(isCharging: Boolean, isObserving: Boolean) {
        saveServiceState(isObserving)
        val lowVolume = isVolumeLow()

        _uiState.value = _uiState.value.copy(
            isCharging = isCharging,
            isObserving = isObserving,
            isVolumeLow = lowVolume,
            buttonEnabled = isCharging || isObserving,
            buttonText = if (isObserving) "Disarm" else "Hold On",
            chargingText = if (isCharging) "Charging" else "Plug in your charger"
        )

    }

    private fun isDeviceCharging(): Boolean {
        val batteryIntent = getApplication<Application>()
            .registerReceiver(null, android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }


    fun setObserving(observing: Boolean, isCharging: Boolean) {
        updateState(isCharging, observing)
    }

    private fun saveServiceState(running: Boolean) {
        prefs.edit { putBoolean("is_running", running) }
    }

    private fun updateVolumeOnly() {
        val low = isVolumeLow()
        _uiState.value = _uiState.value.copy(isVolumeLow = low)
    }

    private fun isVolumeLow(): Boolean {
        val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val percent = (current.toFloat() / max) * 100
        return percent < 50
    }







    override fun onCleared() {
        getApplication<Application>().contentResolver
            .unregisterContentObserver(volumeObserver)
        super.onCleared()
    }
}