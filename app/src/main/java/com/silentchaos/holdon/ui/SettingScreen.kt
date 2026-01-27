package com.silentchaos.holdon.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.silentchaos.holdon.R
import androidx.navigation.NavController
import com.silentchaos.holdon.ui.Components.CustomAlarmSoundDropdown
import com.silentchaos.holdon.ui.Components.SocialCard
import com.silentchaos.holdon.ui.Components.topBarSettingScreen
import com.silentchaos.holdon.utils.SharedPreferencesHelper


@Composable
fun SettingScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val sounds = listOf("Default", "Siren","Machinary")
    val soundResIds = listOf(
        R.raw.alarm_sound,
        R.raw.alarm_sound2,
        R.raw.alarm_sound3
    )
    val selectedSound = remember {
        mutableStateOf(SharedPreferencesHelper.getAlarmSound(context) ?: R.raw.alarm_sound)
    }

   Scaffold(
       topBar = {
           topBarSettingScreen(navController, screenWidth, screenHeight)

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
                   selectedSound = selectedSound,
                   onSoundSelected = { soundId ->
                       selectedSound.value = soundId
                       SharedPreferencesHelper.saveAlarmSound(context, soundId)

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
                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/imgurujeet"))
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
                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/imgurujeet"))
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
                   url = "https://www.linkedin.com/in/gurujeet-k-975b8a288/",
                   onClick = {
                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/gurujeet-k-975b8a288/"))
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
                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/narwhal.25.1.1"))
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
               SocialCard(
                   name = "Email",
                   url = "mailto:imgurujeet@gmail.com",
                   onClick = {
                       val intent = Intent(Intent.ACTION_SENDTO).apply {
                           data = Uri.parse("mailto:imgurujeet@gmail.com")
                       }
                       ContextCompat.startActivity(context, intent, null)
                   },
                   icon = {
                       Icon(
                           painter = painterResource(id = R.drawable.email_social_media_black_icon),
                           contentDescription = "Email Icon",
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
                       text = "Made with 🫠 by Gurujeet",
                       style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Thin),
                       color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                       modifier = Modifier
                           .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp)
                   )
               }

           }



       }


   )
}







