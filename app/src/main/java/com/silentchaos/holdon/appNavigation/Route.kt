package com.silentchaos.holdon.appNavigation


sealed class Route(
    val route: String,
) {
    object Home : Route("home")
    object Setting : Route("setting")
    object Info : Route("info")
}

sealed class ScreenNavigation(
    val route: String,
) {
    object Home : ScreenNavigation(
        route = "home",
    )

    object Setting : ScreenNavigation(
        route = "setting",
    )

    object Info : ScreenNavigation(
        route = "info",
    )
}