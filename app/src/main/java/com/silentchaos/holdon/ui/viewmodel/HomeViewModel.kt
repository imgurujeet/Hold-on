package com.silentchaos.holdon.ui.viewmodel

import android.app.Application
import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
        application.contentResolver.registerContentObserver(
            Settings.System.CONTENT_URI,
            true,
            volumeObserver
        )
    }

    fun updateState(isCharging: Boolean, isObserving: Boolean) {
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