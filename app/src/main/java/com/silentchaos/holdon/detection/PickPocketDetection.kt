package com.silentchaos.holdon.detection

import android.app.KeyguardManager
import android.content.Context
import android.hardware.*
import kotlin.math.sqrt

class PickPocketDetection(
    private val context: Context,
    private val onSuspiciousMovement: () -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var proximity: Sensor? = null

    private var wasCovered = false
    private var lastAcceleration = 0f
    private var uncoverTime = 0L

    fun start() {
        sensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        accelerometer =
            sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        proximity =
            sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        accelerometer?.also {
            sensorManager?.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME // 🔥 faster
            )
        }

        proximity?.also {
            sensorManager?.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
        sensorManager = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        when (event.sensor.type) {

            Sensor.TYPE_PROXIMITY -> {
                val distance = event.values[0]
                val maxRange = proximity?.maximumRange ?: 5f

                val covered = distance < maxRange

                if (wasCovered && !covered) {
                    // Transition: covered → uncovered
                    uncoverTime = System.currentTimeMillis()
                }

                wasCovered = covered
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val movement = sqrt(x * x + y * y + z * z)

                val now = System.currentTimeMillis()

                // More realistic threshold
                if (movement > 5.0f && now - uncoverTime < 2000 &&
                    now - uncoverTime < 2000 // 2 seconds window
                ) {

                    val keyguard =
                        context.getSystemService(Context.KEYGUARD_SERVICE)
                                as KeyguardManager

                    if (keyguard.isDeviceLocked) {
                        onSuspiciousMovement()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}