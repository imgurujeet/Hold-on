package com.silentchaos.holdon.ui.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.silentchaos.holdon.appNavigation.Route

@Composable
fun TopBar(navController: NavController, screenWidth: Int, screenHeight: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.systemBars)

    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "info Icon",
            modifier = Modifier
                .width(screenWidth.dp * 0.05f)
                .height(screenHeight.dp * 0.05f)
                .align (Alignment.CenterStart)
                .clickable(
                    onClick = { navController.navigate(Route.Info.route) } // Replace with your navigation logic
                ),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

        Icon(
            imageVector =Icons.Default.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier
                .width(screenWidth.dp * 0.05f)
                .height(screenHeight.dp * 0.05f)
                .align(Alignment.CenterEnd)
                .clickable(
                    onClick = { navController.navigate(Route.Setting.route) } // Replace with your navigation logic
                ),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}