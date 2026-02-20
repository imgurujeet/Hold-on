package com.silentchaos.holdon.data.preferences

import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    val alarmSound: Flow<Int>

    suspend fun setAlarmSound(resId: Int)
}