package com.silentchaos.holdon.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.silentchaos.holdon.ui.HomeScreen
import com.silentchaos.holdon.ui.InfoScreen
import com.silentchaos.holdon.ui.SettingScreen


@Composable
fun NavGraph() {
    val context = LocalContext.current
    val navController = rememberNavController()
     NavHost(navController = navController, startDestination = Route.Home.route) {
         composable(Route.Home.route) { HomeScreen(navController) }
         composable(Route.Setting.route) { SettingScreen(navController) }
         composable(Route.Info.route) { InfoScreen(navController) }
     }
}