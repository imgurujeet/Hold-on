package com.silentchaos.holdon.detection

import android.app.KeyguardManager
import android.content.Context
import android.hardware.*
import kotlin.math.sqrt

data class PickPocketConfig(
    val motionThreshold: Float,
    val verificationDelay: Long
)

class PickPocketDetection(
    private val context: Context,
    private val config: PickPocketConfig,
    private val onSuspiciousMovement: () -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private val proximity: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    private var wasCovered = false
    private var uncoverTime = 0L
    private var isRunning = false

    private var motionStartTime = 0L
    private var motionActive = false

    val hasAccelerometer: Boolean
        get() = accelerometer != null

    val hasProximity: Boolean
        get() = proximity != null

    fun start() {

        if (!hasAccelerometer) {
            // Cannot detect without motion sensor
            return
        }

        if (isRunning) return
        isRunning = true

        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        proximity?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        if (!isRunning) return
        sensorManager.unregisterListener(this)
        isRunning = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {

            Sensor.TYPE_PROXIMITY -> {

                if (!hasProximity) return

                val distance = event.values[0]
                val maxRange = proximity?.maximumRange ?: 5f

                val covered = distance < maxRange

                if (wasCovered && !covered) {
                    // Covered → Uncovered transition
                    uncoverTime = System.currentTimeMillis()
                }

                wasCovered = covered
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {

                if (!hasAccelerometer) return

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val movement = sqrt(x * x + y * y + z * z)
                val now = System.currentTimeMillis()

                // Smart fallback logic
                val effectiveThreshold =
                    if (hasProximity)
                        config.motionThreshold
                    else
                        config.motionThreshold * 1.4f // Increase if no proximity

                val withinWindow =
                    if (hasProximity)
                        now - uncoverTime < config.verificationDelay
                    else
                        true // No proximity → rely only on motion



                if (movement > effectiveThreshold && withinWindow) {

                    if (!motionActive) {
                        motionActive = true
                        motionStartTime = now
                    }

                    val sustained = now - motionStartTime > 200 // 200ms sustained motion

                    if (sustained) {

                        val keyguard =
                            context.getSystemService(Context.KEYGUARD_SERVICE)
                                    as KeyguardManager

                        if (keyguard.isDeviceLocked) {
                            onSuspiciousMovement()
                        }

                        motionActive = false
                    }

                } else {
                    motionActive = false
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}