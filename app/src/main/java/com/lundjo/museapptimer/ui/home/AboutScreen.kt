package com.lundjo.museapptimer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "About",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        InfoCard(
            title = "What is this app?",
            content = "Muse is a minimalistic app blocker designed to help you stay focused and maintain a healthy digital balance. Create custom bundles of apps and set specific time windows when they should be blocked."
        )
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(
            title = "How to use",
            content = "1. Create a Bundle\nTap the grid icon to select apps you want to block together.\n\n2. Set Schedule\nTap the clock icon to view your bundles and set blocking times.\n\n3. Configure Settings Hour\nChoose one hour per day when you can modify your blocking schedules."
        )
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(
            title = "Variable Timer Feature",
            content = "Each time you open an app, you'll see a variable timer (randomly 20, 40, or 60 seconds) — a moment to reflect on whether you really need to use the app right now."
        )
        Spacer(modifier = Modifier.height(12.dp))
        InfoCard(
            title = "Tips",
            content = "• The app uses your phone's timezone and time settings\n• Settings can only be changed during your selected hour\n• Create separate bundles for work hours and personal time\n• Block social media during study or work sessions\n• Set up a bedtime bundle to improve sleep quality"
        )
    }
}

@Composable
private fun InfoCard(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}