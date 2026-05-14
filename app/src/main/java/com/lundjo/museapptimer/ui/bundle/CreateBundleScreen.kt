package com.lundjo.museapptimer.ui.bundle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.lundjo.museapptimer.data.getInstalledApps

@Composable
fun CreateBundleScreen(viewModel: BundleViewModel) {
    var bundleName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val installedApps = remember { getInstalledApps(context) }
    var selectedApps by remember { mutableStateOf(setOf<String>()) }
    val bundles by viewModel.bundles.collectAsState()
    val bundledPackageNames by viewModel.bundledPackageNames.collectAsState()
    val availableApps = installedApps.filter {
        !bundledPackageNames.contains(it.packageName)
    }
    val isButtonEnabled = bundleName.isNotBlank() &&
            selectedApps.isNotEmpty() &&
            bundles.none { it.name == bundleName }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Create Bundle",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = bundleName,
            onValueChange = { bundleName = it },
            placeholder = { Text("Bundle Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(availableApps) { app ->
                AppGridItem(
                    name = app.displayName,
                    packageName = app.packageName,
                    isSelected = selectedApps.contains(app.packageName),
                    onClick = {
                        selectedApps = if (selectedApps.contains(app.packageName)) {
                            selectedApps - app.packageName
                        } else {
                            selectedApps + app.packageName
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.createBundle(bundleName, selectedApps, installedApps)
                bundleName = ""
                selectedApps = emptySet()
            },
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Create Bundle (${selectedApps.size} apps)")
        }
    }
}

@Composable
private fun AppGridItem(
    name: String,
    packageName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val icon = remember(packageName) {
        try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isSelected) Color.White
                    else MaterialTheme.colorScheme.surface
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Image(
                    painter = rememberAsyncImagePainter(icon),
                    contentDescription = name,
                    modifier = Modifier.size(56.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Android,
                    contentDescription = name,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}