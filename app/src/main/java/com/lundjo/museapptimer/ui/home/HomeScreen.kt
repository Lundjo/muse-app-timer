package com.lundjo.museapptimer.ui.home

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.provider.Settings
import androidx.compose.material3.Text


@Composable
private fun MenuCard(
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun HomeScreen(
    onBundlesClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val context = LocalContext.current
    val isServiceEnabled = remember {
        isAccessibilityServiceEnabled(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (!isServiceEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        context.startActivity(intent)
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tap to enable accessibility service",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuCard(
                icon = Icons.Outlined.Apps,
                contentDescription = "Bundles",
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
                    .clickable { onBundlesClick() }
            )
            MenuCard(
                icon = Icons.Outlined.AccessTime,
                contentDescription = "Time & Bundles",
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
                    .clickable { onScheduleClick() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        MenuCard(
            icon = Icons.Outlined.Settings,
            contentDescription = "Settings",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable { onSettingsClick() }
        )
    }
}

private fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabledServices.contains(context.packageName)
}