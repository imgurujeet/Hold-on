package com.silentchaos.holdon.ui.pickPocketAlert

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.silentchaos.holdon.biometric.Authenticator
import com.silentchaos.holdon.engine.AlertType
import com.silentchaos.holdon.engine.SecurityEngine
import com.silentchaos.holdon.engine.SecurityEngineService
import com.silentchaos.holdon.engine.SecurityState
import com.silentchaos.holdon.ui.alarmAlert.AlarmAlertScreen
import com.silentchaos.holdon.ui.theme.HoldOnTheme

class PickPocketAlertActivity : FragmentActivity() {

    private val authenticator = Authenticator()

    private var remainingSeconds by mutableStateOf(5)
    private var timer: CountDownTimer? = null
    private var verificationTimeMillis = 5000L// 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Close activity if alert state changes
        lifecycleScope.launchWhenStarted {
            SecurityEngine.state.collect { state ->
                if (state !is SecurityState.Alert ||
                    state.type != AlertType.PICKPOCKET
                ) {
                    finish()
                }
            }
        }

        // Show above lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        // Keep screen awake
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Disable back
        onBackPressedDispatcher.addCallback(this) {}

        setContent {
            HoldOnTheme {
                AlarmAlertScreen(
                    countdownSeconds = remainingSeconds,
                    title = "PickPocket Detection",
                    subtitle = "Authenticate within time to prevent alarm",
                    onAuthenticate = {
                        authenticateAndStop()
                    }
                )
            }
        }

        startVerificationTimer()
    }

    private fun startVerificationTimer() {

        verificationTimeMillis = SecurityEngine.currentVerificationDelay

        val totalSeconds = (verificationTimeMillis / 1000).toInt()
        remainingSeconds = totalSeconds

        timer = object : CountDownTimer(
            verificationTimeMillis,
            1000
        ) {

            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                remainingSeconds = 0
                SecurityEngine.triggerPickPocketAlarm()
            }

        }.start()
    }

    private fun authenticateAndStop() {

        authenticator.authenticate(
            activity = this,
            onSuccess = {

                timer?.cancel()

                // Inform engine authentication success
                SecurityEngine.onPickPocketAuthenticationSuccess()

                // Stop service
                val stopIntent = Intent(
                    this,
                    SecurityEngineService::class.java
                ).apply {
                    action = SecurityEngineService.ACTION_STOP
                }

                startService(stopIntent)

                finish()
            }
        )
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}