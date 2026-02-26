package com.silentchaos.holdon.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.silentchaos.holdon.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")


class AppPreferencesImpl(
    private val context: Context
) : AppPreferences {

    companion object {
        private val KEY_ALARM_SOUND = intPreferencesKey("alarm_sound")
        private val KEY_PICKPOCKET_MODE = stringPreferencesKey("pickpocket_mode")
        private val KEY_MOTION_THRESHOLD = floatPreferencesKey("motion_threshold")
        private val KEY_VERIFICATION_DELAY = longPreferencesKey("verification_delay")
    }

    override val alarmSound: Flow<Int>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_ALARM_SOUND] ?: R.raw.alarm_sound
        }

    override suspend fun setAlarmSound(resId: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ALARM_SOUND] = resId
        }
    }


    override val pickPocketMode: Flow<String>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_PICKPOCKET_MODE] ?: "normal"
        }

    override val motionThreshold: Flow<Float>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_MOTION_THRESHOLD] ?: 5.0f
        }

    override val verificationDelay: Flow<Long>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_VERIFICATION_DELAY] ?: 2000L
        }

    override suspend fun setPickPocketMode(mode: String) {
        context.dataStore.edit { it[KEY_PICKPOCKET_MODE] = mode }
    }

    override suspend fun setMotionThreshold(value: Float) {
        context.dataStore.edit { it[KEY_MOTION_THRESHOLD] = value }
    }

    override suspend fun setVerificationDelay(value: Long) {
        context.dataStore.edit { it[KEY_VERIFICATION_DELAY] = value }
    }
}