package com.silentchaos.holdon.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SocialCard(
    name: String,
    url: String,
    onClick: (String) -> Unit = {},
    icon: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // thin card
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    )
                )
            )

            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Align text and icon vertically
            horizontalArrangement = Arrangement.Center // Center the entire row horizontally
        ) {
            icon() // Your icon composable
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterVertically) // Ensure text aligns with icon center
            )
            Spacer(modifier = Modifier.weight(1f)) // Pushes the arrow to the end


        Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Open $name",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .clickable { onClick(url) }
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp)) // Add space below the card
}
