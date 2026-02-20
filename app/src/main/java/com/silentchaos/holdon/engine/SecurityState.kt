package com.silentchaos.holdon.engine

sealed interface SecurityState {

    data object Idle : SecurityState

    data object Monitoring : SecurityState

    data class Alert(
        val type: AlertType
    ) : SecurityState
}
