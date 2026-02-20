package com.silentchaos.holdon.engine

import com.silentchaos.holdon.alert.AlertController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SecurityEngine {

    // -----------------------------
    // State
    // -----------------------------

    private val _state =
        MutableStateFlow<SecurityState>(SecurityState.Idle)

    val state: StateFlow<SecurityState> = _state

    // -----------------------------
    // Dependencies
    // -----------------------------

    private var alertController: AlertController? = null

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
        _state.value = SecurityState.Idle
    }

}