package com.silentchaos.holdon.engine

import android.content.Context
import android.content.Intent
import com.silentchaos.holdon.alert.AlertController
import com.silentchaos.holdon.ui.ProtectionModeUI
import com.silentchaos.holdon.ui.pickPocketAlert.PickPocketAlertActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.jvm.java

object SecurityEngine {

    // -----------------------------
    // State
    // -----------------------------

    private val _state =
        MutableStateFlow<SecurityState>(SecurityState.Idle)

    val state: StateFlow<SecurityState> = _state

    private val _mode = MutableStateFlow(ProtectionModeUI.CHARGER)
    val mode: StateFlow<ProtectionModeUI> = _mode

    // -----------------------------
    // PickPocket Config
    // -----------------------------

    var currentVerificationDelay: Long = 5000L
        private set

    fun setMode(mode: ProtectionModeUI) {
        _mode.value = mode
    }

    fun updatePickPocketVerificationDelay(configDelay: Long) {
        currentVerificationDelay = configDelay
    }

    // -----------------------------
    // Dependencies
    // -----------------------------

    private var alertController: AlertController? = null
    private var isVerifying = false

    fun initialize(controller: AlertController) {
        alertController = controller
    }

    // -----------------------------
    // Monitoring Control
    // -----------------------------

    fun startMonitoring() {
        _state.value = SecurityState.Monitoring
    }

    fun stopMonitoring() {
        stopAlert()
        _state.value = SecurityState.Idle
    }

    // -----------------------------
    // Charger Events
    // -----------------------------

    fun onChargerRemoved() {

        // Only trigger alert if monitoring is active
        if (_state.value != SecurityState.Monitoring) return

        _state.value = SecurityState.Alert(AlertType.CHARGER)
        alertController?.showVerification(AlertType.CHARGER)
        alertController?.startAlert(AlertType.CHARGER)
    }


    fun onChargerConnected() {

        if (_state.value is SecurityState.Alert) {
            alertController?.stopAlert()
            _state.value = SecurityState.Monitoring
        }
    }


    // -----------------------------
    // Alert Control
    // -----------------------------

    fun stopAlert() {
        alertController?.stopAlert()
        isVerifying = false
        _state.value = SecurityState.Idle
    }



    //PickPocket detection
    fun onPickPocketDetected() {

        if (isVerifying) return

        isVerifying = true

        _state.value = SecurityState.Alert(AlertType.PICKPOCKET)

        //  Only show verification screen
        alertController?.showVerification(AlertType.PICKPOCKET)
    }

    fun onPickPocketAuthenticationSuccess() {
        isVerifying = false
        _state.value = SecurityState.Idle
    }

    fun triggerPickPocketAlarm() {
        alertController?.startAlert(AlertType.PICKPOCKET)
    }

}