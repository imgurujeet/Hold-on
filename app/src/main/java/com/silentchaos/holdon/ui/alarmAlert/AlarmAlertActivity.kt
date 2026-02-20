package com.silentchaos.holdon.ui.alarmAlert

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.silentchaos.holdon.biometric.Authenticator
import com.silentchaos.holdon.engine.SecurityEngine
import com.silentchaos.holdon.engine.SecurityEngineService
import com.silentchaos.holdon.engine.SecurityState
import com.silentchaos.holdon.ui.alarmAlert.AlarmAlertScreen
import com.silentchaos.holdon.ui.theme.HoldOnTheme

class AlarmAlertActivity : FragmentActivity() {

    private val authenticator = Authenticator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            SecurityEngine.state.collect { state ->
                if (state !is SecurityState.Alert) {
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

        // Keep screen on
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        // Disable back button
        onBackPressedDispatcher.addCallback(this) {}

        setContent {
            HoldOnTheme {
                AlarmAlertScreen(
                    onAuthenticate = {
                        authenticateAndStop()
                    }
                )
            }
        }

        // If launched from notification STOP action
        if (intent?.action == "AUTH_FROM_NOTIFICATION") {
            authenticateAndStop()
        }
    }

    private fun authenticateAndStop() {

        authenticator.authenticate(
            activity = this,
            onSuccess = {
                SecurityEngine.stopMonitoring()
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
}
