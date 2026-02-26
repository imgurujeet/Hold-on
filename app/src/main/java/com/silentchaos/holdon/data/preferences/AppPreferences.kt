package com.silentchaos.holdon.data.preferences

import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    val alarmSound: Flow<Int>

    val pickPocketMode: Flow<String>
    val motionThreshold: Flow<Float>
    val verificationDelay: Flow<Long>

    suspend fun setAlarmSound(resId: Int)

    suspend fun setPickPocketMode(mode: String)
    suspend fun setMotionThreshold(value: Float)
    suspend fun setVerificationDelay(value: Long)
}