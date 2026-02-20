package com.silentchaos.holdon.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.silentchaos.holdon.R
import com.silentchaos.holdon.data.preferences.AppPreferencesImpl
import com.silentchaos.holdon.utils.AppInfoProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val selectedSound: Int = R.raw.alarm_sound,
    val versionInfo: String = ""
)


class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val preferences =
        AppPreferencesImpl(application.applicationContext)

    private val versionInfo =
        AppInfoProvider.getAppVersion(application)

    val versionInfoFormatted = versionInfo.formatted()

    val uiState: StateFlow<SettingsUiState> =
        preferences.alarmSound
            .map { sound ->
                SettingsUiState(
                    selectedSound = sound,
                    versionInfo = versionInfoFormatted
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SettingsUiState(
                    selectedSound = R.raw.alarm_sound,
                    versionInfo = versionInfoFormatted
                )
            )


    fun selectSound(soundId: Int) {
        viewModelScope.launch {
            preferences.setAlarmSound(soundId)
        }
    }
}

