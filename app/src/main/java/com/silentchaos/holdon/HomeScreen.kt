package com.silentchaos.holdon

import android.R.attr.icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,

        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .windowInsetsPadding(WindowInsets.systemBars)

            ) {
               Icon(
                   imageVector = Icons.Default.Info,
                   contentDescription = "App Icon",
                   modifier = Modifier
                       .width(screenWidth.dp * 0.05f)
                       .height(screenHeight.dp * 0.05f)
                       .align (Alignment.CenterStart),
                   tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                Icon(
                    imageVector =Icons.Default.Settings,
                    contentDescription = "Settings Icon",
                    modifier = Modifier
                        .width(screenWidth.dp * 0.05f)
                        .height(screenHeight.dp * 0.05f)
                        .align(Alignment.CenterEnd),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        },
        bottomBar = {
            Button(onClick ={},
                Modifier.padding(horizontal = 16.dp)
                    .height(screenHeight.dp * 0.08f)
                    .windowInsetsPadding(WindowInsets.navigationBars),

            ) {
                Text(
                    text = "Hold On",
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center

                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.smartphone),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .width(screenWidth.dp * 0.2f)
                        .height(screenHeight.dp * 0.2f),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    )
}
