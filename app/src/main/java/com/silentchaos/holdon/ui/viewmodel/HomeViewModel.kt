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
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.engine.SecurityEngine
import com.silentchaos.holdon.engine.SecurityState
import com.silentchaos.holdon.ui.ProtectionModeUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class HomeUiState(
    val selectedMode: ProtectionModeUI = ProtectionModeUI.CHARGER,
    val isCharging: Boolean = false,
    val isMonitoring: Boolean = false,
    val isAlertActive: Boolean = false,
    val isVolumeLow: Boolean = false,
    val volumePercent: Int = 0,
    val batteryPercent: Int = 0,
    val buttonEnabled: Boolean = false,
    val buttonText: String = "Activate",
    val pickPocketMode: String = "normal",
    val motionThreshold: Float = 5f,
    val verificationDelay: Long = 2000L
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application
    private val preferences = AppPreferencesImpl(app)

    private val audioManager =
        app.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun setMode(mode: ProtectionModeUI) {
        SecurityEngine.setMode(mode)
    }

    private fun updateButtonState() {

        val state = _uiState.value

        val enabled = when (state.selectedMode) {

            ProtectionModeUI.CHARGER -> {
                if (state.isMonitoring) {
                    true
                } else {
                    state.isCharging
                }
            }

            ProtectionModeUI.PICKPOCKET -> {
                if (state.isMonitoring) {
                    true
                } else {
                    !state.isCharging   //  Only disable when charging
                }
            }
        }

        val text = when {
            state.isMonitoring -> "Disarm"
            else -> "Hold On"

        }

        _uiState.value = state.copy(
            buttonEnabled = enabled,
            buttonText = text
        )
    }

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

        viewModelScope.launch {
            preferences.pickPocketMode.collect { mode ->

                val (finalThreshold, finalDelay) = when (mode) {
                    "high" -> 3f to 1000L
                    "crowded" -> 5f to 3000L
                    "normal" -> 8f to 8000L
                    "custom" -> {
                        val threshold = preferences.motionThreshold.first()
                        val delay = preferences.verificationDelay.first()
                        threshold to delay
                    }
                    else -> 8f to 8000L
                }
                if (mode != "custom") {
                    preferences.setMotionThreshold(finalThreshold)
                    preferences.setVerificationDelay(finalDelay)
                }

                _uiState.value = _uiState.value.copy(
                    pickPocketMode = mode,
                    motionThreshold = finalThreshold,
                    verificationDelay = finalDelay
                )

            }
        }



        viewModelScope.launch {
            SecurityEngine.mode.collect { mode ->
                _uiState.value = _uiState.value.copy(
                    selectedMode = mode
                )
                updateButtonState()
            }
        }
        app.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
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

                when (state) {

                    SecurityState.Idle -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = false,
                            isAlertActive = false
                        )
                    }

                    SecurityState.Monitoring -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = true,
                            isAlertActive = false
                        )
                    }

                    is SecurityState.Alert -> {
                        _uiState.value = _uiState.value.copy(
                            isMonitoring = true,
                            isAlertActive = true
                        )
                    }
                }

                updateButtonState()
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
            batteryPercent = batteryPercent
        )

        updateButtonState()
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


    //pickpocket sensor value

    fun setPickPocketMode(mode: String) {
        viewModelScope.launch {
            preferences.setPickPocketMode(mode)
        }
    }

    fun setMotionThreshold(value: Float) {
        viewModelScope.launch {
            preferences.setMotionThreshold(value)
        }
    }

    fun setVerificationDelay(value: Long) {
        viewModelScope.launch {
            preferences.setVerificationDelay(value)
        }
    }

}