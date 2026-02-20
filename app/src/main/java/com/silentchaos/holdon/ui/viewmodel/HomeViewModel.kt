package com.silentchaos.holdon.ui.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.silentchaos.holdon.engine.SecurityEngine
import com.silentchaos.holdon.engine.SecurityState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HomeUiState(
    val isCharging: Boolean = false,
    val isMonitoring: Boolean = false,
    val isAlertActive: Boolean = false,
    val isVolumeLow: Boolean = false,
    val volumePercent: Int = 0,
    val batteryPercent: Int = 0,
    val buttonEnabled: Boolean = false,
    val buttonText: String = "Hold On",
    val chargingText: String = "Plug in your charger"
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    private val audioManager =
        app.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    // ---------------------------
    // Battery Receiver
    // ---------------------------

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateChargingState()
        }
    }

    private val volumeObserver = object : ContentObserver(
        Handler(Looper.getMainLooper())
    ) {
        override fun onChange(selfChange: Boolean) {
            updateVolumeOnly()
        }
    }


    init {
        app.contentResolver.registerContentObserver(
            Settings.System.getUriFor(Settings.System.ALARM_ALERT),
            true,
            volumeObserver
        )

        // Observe charging changes
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        app.registerReceiver(batteryReceiver, filter)

        observeEngineState()

        // Initial sync
        updateChargingState()
        updateVolumeOnly()

    }


    private fun updateVolumeOnly() {
        val current = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)

        val percent = ((current.toFloat() / max) * 100).toInt()

        _uiState.value = _uiState.value.copy(
            isVolumeLow = percent < 50,
            volumePercent = percent
        )
    }

    fun setAlarmVolumeFromPercent(percent: Float) {
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)

        val newVolume = ((percent / 100f) * max).toInt()

        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            newVolume,
            0
        )

        _uiState.value = _uiState.value.copy(
            volumePercent = percent.toInt(),
            isVolumeLow = percent < 50
        )
    }

    // ---------------------------------------
    // Observe SecurityEngine
    // ---------------------------------------

    private fun observeEngineState() {
        viewModelScope.launch {
            SecurityEngine.state.collect { state ->

                val charging = _uiState.value.isCharging

                when (state) {

                    SecurityState.Idle -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = false,
                            isAlertActive = false,
                            buttonEnabled = charging,
                            buttonText = "Hold On",
                            chargingText = if (charging) "Charging"
                            else "Plug in your charger"
                        )
                    }

                    SecurityState.Monitoring -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = true,
                            isAlertActive = false,
                            buttonEnabled = true,
                            buttonText = "Disarm",
                            chargingText = if (charging) "Charging"
                            else "Plug in your charger"
                        )
                    }

                    is SecurityState.Alert -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = true,
                            isAlertActive = true,
                            buttonEnabled = true,
                            buttonText = "Disarm",
                            chargingText = "Charger Removed!"
                        )
                    }
                }
            }
        }
    }

    // ---------------------------------------
    // Charging
    // ---------------------------------------

    private fun updateChargingState() {

        val batteryIntent = app.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val status = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            -1
        )

        val charging =
            status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL

        val level = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_LEVEL,
            0
        ) ?: 0

        val scale = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_SCALE,
            100
        ) ?: 100

        val batteryPercent = (level * 100) / scale

        _uiState.value = _uiState.value.copy(
            isCharging = charging,
            batteryPercent = batteryPercent,
            chargingText = if (_uiState.value.isAlertActive) {
                "Charger Removed!"
            } else {
                if (charging) "Charging"
                else "Plug in your charger"
            },
            buttonEnabled = if (_uiState.value.isMonitoring) true else charging
        )
    }

    private fun isDeviceCharging(): Boolean {
        val batteryIntent = app.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val status = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            -1
        )

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
    }

    override fun onCleared() {
        app.unregisterReceiver(batteryReceiver)
        app.contentResolver.unregisterContentObserver(volumeObserver)
        super.onCleared()
    }
}