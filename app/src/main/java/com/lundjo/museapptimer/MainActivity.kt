package com.lundjo.museapptimer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.provider.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lundjo.museapptimer.navigation.AppNavigation
import com.lundjo.museapptimer.ui.theme.MuseAppTimerTheme
import androidx.core.net.toUri
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            MuseAppTimerTheme {
                var canDrawOverlays by remember { mutableStateOf(Settings.canDrawOverlays(this)) }

                if (!canDrawOverlays) {
                    LaunchedEffect(Unit) {
                        while (!canDrawOverlays) {
                            delay(500)
                            canDrawOverlays = Settings.canDrawOverlays(this@MainActivity)
                        }
                    }
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { },
                        title = { androidx.compose.material3.Text("Permission required") },
                        text = { androidx.compose.material3.Text("Muse App Timer needs overlay permission to show the timer. You will be redirected to Settings.") },
                        confirmButton = {
                            androidx.compose.material3.TextButton(onClick = {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    "package:$packageName".toUri()
                                )
                                startActivity(intent)
                            }) {
                                androidx.compose.material3.Text("Continue")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.TextButton(onClick = { finish() }) {
                                androidx.compose.material3.Text("Cancel")
                            }
                        }
                    )
                }
                var isUsageStatsGranted by remember { mutableStateOf(isUsageStatsPermissionGranted()) }

                if (!isUsageStatsGranted) {
                    LaunchedEffect(Unit) {
                        while (!isUsageStatsGranted) {
                            delay(500)
                            isUsageStatsGranted = isUsageStatsPermissionGranted()
                        }
                    }
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { },
                        title = { androidx.compose.material3.Text("Permission required") },
                        text = { androidx.compose.material3.Text("Muse App Timer needs usage access to track app usage. You will be redirected to Settings.") },
                        confirmButton = {
                            androidx.compose.material3.TextButton(onClick = {
                                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                startActivity(intent)
                            }) {
                                androidx.compose.material3.Text("Continue")
                            }
                        },
                        dismissButton = {
                            androidx.compose.material3.TextButton(onClick = { finish() }) {
                                androidx.compose.material3.Text("Cancel")
                            }
                        }
                    )
                }
                AppNavigation()
            }
        }
    }

    private fun isUsageStatsPermissionGranted(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOps.checkOpNoThrow(
            android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            packageName
        )
        return mode == android.app.AppOpsManager.MODE_ALLOWED
    }
}