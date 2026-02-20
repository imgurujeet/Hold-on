package com.silentchaos.holdon.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
}