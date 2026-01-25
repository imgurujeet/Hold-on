package com.silentchaos.holdon.navigation


sealed class Route(
    val route: String,
) {
    object Home : Route("home")
    object Setting : Route("setting")
    object Info : Route("info")
}
