package com.lundjo.museapptimer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import com.lundjo.museapptimer.navigation.AppNavigation
import com.lundjo.museapptimer.ui.theme.MuseAppTimerTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {

    private val canDrawOverlays = mutableStateOf(false)
    private val isUsageStatsGranted = mutableStateOf(false)
    private val isAccessibilityEnabled = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            MuseAppTimerTheme {
                if (!canDrawOverlays.value) {
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
                else if (!isUsageStatsGranted.value) {
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
                else if (!isAccessibilityEnabled.value) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = { },
                        title = { androidx.compose.material3.Text("Permission required") },
                        text = { androidx.compose.material3.Text("Muse App Timer needs accessibility permission to block apps. You will be redirected to Settings.") },
                        confirmButton = {
                            androidx.compose.material3.TextButton(onClick = {
                                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
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

    override fun onResume() {
        super.onResume()
        canDrawOverlays.value = Settings.canDrawOverlays(this)
        isUsageStatsGranted.value = isUsageStatsPermissionGranted()
        isAccessibilityEnabled.value = isAccessibilityServiceEnabled()
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

    private fun isAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.contains(packageName)
    }
}