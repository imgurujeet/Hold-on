package com.silentchaos.holdon.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.R
import com.silentchaos.holdon.ui.Components.CustomAlarmSoundDropdown
import com.silentchaos.holdon.ui.Components.SocialCard
import com.silentchaos.holdon.ui.Components.TopBarSettingScreen
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silentchaos.holdon.ui.viewmodel.SettingsViewModel


@Composable
fun SettingScreen(onBackClick :() -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val viewModel: SettingsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var showFeedbackDialog by remember { mutableStateOf(false) }




    val sounds = listOf("Default", "Siren","Machinary")
    val soundResIds = listOf(
        R.raw.alarm_sound,
        R.raw.alarm_sound2,
        R.raw.alarm_sound3
    )

   Scaffold(
       topBar = {
           TopBarSettingScreen(onBackClick, screenWidth, screenHeight)

       },
       content = { innerPadding ->
           Column(
               modifier = Modifier
                   .padding(innerPadding)
                   .fillMaxSize()
                   .padding(horizontal = 16.dp)
           ) {

               CustomAlarmSoundDropdown(
                   sounds = sounds,
                   soundResIds = soundResIds,
                   selectedSound = uiState.selectedSound.let { mutableStateOf(it) },
                   onSoundSelected = { soundId ->
                       viewModel.selectSound(soundId)
                   }
               )
                Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = "Socials",
                   style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                   color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
               )
                Spacer(modifier = Modifier.height(8.dp))
               SocialCard(
                   name = "Twitter",
                   url = "https://x.com/imgurujeet",
                   onClick = {
                       // Handle Twitter click
                       val intent = Intent(Intent.ACTION_VIEW, "https://x.com/imgurujeet".toUri())
                       ContextCompat.startActivity(context, intent, null)
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.x_social_media_black_icon),
                           contentDescription = "Twitter Icon",
                           tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                           modifier = Modifier.size(
                               width = (screenWidth * 0.06f).dp,
                               height = (screenHeight * 0.06f).dp
                           )
                       )
                   }
               )
               // GitHub
               SocialCard(
                   name = "GitHub",
                   url = "https://github.com/imgurujeet",
                   onClick = {
                       val intent = Intent(Intent.ACTION_VIEW,
                           "https://github.com/imgurujeet".toUri())
                       ContextCompat.startActivity(context, intent, null)
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.github_social_media_black_icon),
                           contentDescription = "GitHub Icon",
                           tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                           modifier = Modifier.size(
                               width = (screenWidth * 0.06f).dp,
                               height = (screenHeight * 0.06f).dp
                           )
                       )
                   }
               )

// LinkedIn
               SocialCard(
                   name = "LinkedIn",
                   url = "https://www.linkedin.com/in/imgurujeet/",
                   onClick = {
                       val intent = Intent(Intent.ACTION_VIEW,
                           "https://www.linkedin.com/in/imgurujeet/".toUri())
                       ContextCompat.startActivity(context, intent, null)
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.linkedin_social_media_black_icon),
                           contentDescription = "LinkedIn Icon",
                           tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                           modifier = Modifier.size(
                               width = (screenWidth * 0.06f).dp,
                               height = (screenHeight * 0.06f).dp
                           )
                       )
                   }
               )

            // Instagram
               SocialCard(
                   name = "Instagram",
                   url = "https://www.instagram.com/imgurujeet",
                   onClick = {
                       val intent = Intent(Intent.ACTION_VIEW,
                           "https://www.instagram.com/imgurujeet".toUri())
                       ContextCompat.startActivity(context, intent, null)
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.instagram_social_media_black_icon),
                           contentDescription = "Instagram Icon",
                           tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                           modifier = Modifier.size(
                               width = (screenWidth * 0.06f).dp,
                               height = (screenHeight * 0.06f).dp
                           )
                       )
                   }
               )

// Email
               Spacer(modifier = Modifier.height(24.dp))

               Text(
                   text = "Support",
                   style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                   color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
               )

               Spacer(modifier = Modifier.height(8.dp))

               SocialCard(
                   name = "Give Feedback",
                   url = "",
                   onClick = { showFeedbackDialog = true },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.email_social_media_black_icon),
                           contentDescription = "Feedback Icon",
                           tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                           modifier = Modifier.size(
                               width = (screenWidth * 0.06f).dp,
                               height = (screenHeight * 0.06f).dp
                           )
                       )
                   }
               )

               Column(
                   modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                   verticalArrangement = Arrangement.Bottom,
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   Text(
                       text = "HoldOn ${uiState.versionInfo}",
                       style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Thin),
                       color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                       modifier = Modifier
                           .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp)
                   )
               }

               if (showFeedbackDialog) {
                   FeedbackDialog(
                       onDismiss = { showFeedbackDialog = false },
                   )
               }

           }



       }


   )
}







@Composable
fun FeedbackDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf("Report Issue") }
    var message by remember { mutableStateOf("") }
    var includeLogs by remember { mutableStateOf(false) }

    val options = listOf(
        "Report Issue",
        "Share an Idea"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Send Feedback",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {

                // ---------- Horizontal Selection ----------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    options.forEach { option ->
                        val isSelected = selectedType == option

                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = if (isSelected) 4.dp else 0.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedType = option
                                    if (option != "Report Issue") {
                                        includeLogs = false
                                    }
                                }
                               ,

                        ) {
                            Text(
                                text = option,
                                modifier = Modifier.padding(
                                    vertical = 12.dp,
                                    horizontal = 8.dp
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------- Message ----------
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Your Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ---------- Include Logs (Only for Report Issue) ----------
                if (selectedType == "Report Issue") {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { includeLogs = !includeLogs }
                    ) {
                        Checkbox(
                            checked = includeLogs,
                            onCheckedChange = { includeLogs = it }
                        )
                        Text(
                            text = "Include debug logs",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = "Send",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(enabled = message.isNotBlank()) {

                        val deviceInfo = if (includeLogs && selectedType == "Report Issue") {

                            val packageInfo = context.packageManager
                                .getPackageInfo(context.packageName, 0)

                            """
                    
                    -------- Device Information --------
                    App Version: ${packageInfo.versionName}
                    Version Code: ${packageInfo.longVersionCode}
                    
                    Manufacturer: ${android.os.Build.MANUFACTURER}
                    Model: ${android.os.Build.MODEL}
                    Brand: ${android.os.Build.BRAND}
                    
                    Android Version: ${android.os.Build.VERSION.RELEASE}
                    SDK Level: ${android.os.Build.VERSION.SDK_INT}
                    
                    Device: ${android.os.Build.DEVICE}
                    Product: ${android.os.Build.PRODUCT}
                    Hardware: ${android.os.Build.HARDWARE}
                    
                    ------------------------------------
                    
                    """.trimIndent()

                        } else ""

                        val fullMessage = """
                    $message
                    $deviceInfo
                """.trimIndent()

                        val uri = android.net.Uri.Builder()
                            .scheme("mailto")
                            .opaquePart("imgurujeet@gmail.com")
                            .appendQueryParameter("subject", selectedType)
                            .appendQueryParameter("body", fullMessage)
                            .build()

                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = uri
                        }

                        context.startActivity(intent)

                        context.startActivity(
                            Intent.createChooser(intent, "Send Feedback")
                        )

                        context.startActivity(intent)
                        onDismiss()
                    },
                color = if (message.isNotBlank())
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        },
        dismissButton = {
            Text(
                text = "Cancel",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}