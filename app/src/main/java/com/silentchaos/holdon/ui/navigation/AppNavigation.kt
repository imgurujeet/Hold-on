package com.silentchaos.holdon.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.silentchaos.holdon.ui.HomeScreen
import com.silentchaos.holdon.ui.InfoScreen
import com.silentchaos.holdon.ui.SettingScreen


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(
        navConfig,
        Home
    )

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->

            when (key) {
                Home -> NavEntry(key) {
                    HomeScreen(
                        onInfoClick = { backStack.add(Info) },
                        onSettingClick = { backStack.add(Setting) }
                    )
                }

                Info -> NavEntry(key) {
                    InfoScreen(
                        onBackClick = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }

                Setting -> NavEntry(key) {
                    SettingScreen(
                        onBackClick = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }

                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }
    )
}
