package com.silentchaos.holdon.utils

import android.content.Context
import com.silentchaos.holdon.R


object SharedPreferencesHelper {

    private const val PREFS_NAME = "AlarmPrefs"
    private const val KEY_ALARM_SOUND = "alarm_sound"

    // Save selected alarm sound
    fun saveAlarmSound(context: Context, resId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_ALARM_SOUND, resId).apply()
    }

    // Get selected alarm sound, default to R.raw.alarm_sound
    fun getAlarmSound(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_ALARM_SOUND, R.raw.alarm_sound)
    }
}