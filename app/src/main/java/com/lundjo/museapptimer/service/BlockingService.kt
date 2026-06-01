package com.lundjo.museapptimer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.lundjo.museapptimer.MuseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("AccessibilityPolicy")
class BlockingService : AccessibilityService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val museApp get() = applicationContext as MuseApp
    private val timedApps = mutableSetOf<String>()
    private val overlayLifecycleOwner = OverlayLifecycleOwner()
    private val windowManager get() = getSystemService(WINDOW_SERVICE) as android.view.WindowManager
    private var overlayView: android.view.View? = null
    private var currentForegroundPackage = ""

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        serviceInfo = info
        overlayLifecycleOwner.onCreate()
        overlayLifecycleOwner.onStart()
        overlayLifecycleOwner.onResume()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName) return
        if (packageName == "com.lundjo.museapptimer") return

        // Home screen or launcher — user left the app, reset so next launch shows timer
        if (isLauncher(packageName)) {
            timedApps.remove(currentForegroundPackage)
            currentForegroundPackage = ""
            return
        }

        if (!hasLauncherIntent(packageName)) return
        if (packageName == "com.android.settings") {
            timedApps.remove(currentForegroundPackage)
            currentForegroundPackage = packageName
            return
        }
        if (packageName == currentForegroundPackage) return

        // User left the previous app — remove it so its next launch triggers a timer
        timedApps.remove(currentForegroundPackage)
        currentForegroundPackage = packageName

        scope.launch {
            val app = applicationContext as MuseApp
            val apps = app.database.appDao().getAllApps().first()
            val blockedApp = apps.find { it.packageName == packageName }

            if (blockedApp != null) {
                val schedule = app.database.scheduleDao()
                    .getSchedulesForBundle(blockedApp.bundleId).first()
                    .firstOrNull() ?: return@launch
                if (isCurrentlyBlocked(schedule.startTime, schedule.endTime, schedule.daysOfWeek)) {
                    goHome()
                }
            } else {
                showTimer(packageName)
            }
        }
    }

    private fun isCurrentlyBlocked(startTime: String, endTime: String, daysOfWeek: String): Boolean {
        val now = LocalTime.now()
        val start = LocalTime.parse(startTime, timeFormatter)
        val end = LocalTime.parse(endTime, timeFormatter)
        val currentDay = java.time.DayOfWeek.from(java.time.LocalDate.now())
        val dayMap = mapOf(
            java.time.DayOfWeek.MONDAY to "MON",
            java.time.DayOfWeek.TUESDAY to "TUE",
            java.time.DayOfWeek.WEDNESDAY to "WED",
            java.time.DayOfWeek.THURSDAY to "THU",
            java.time.DayOfWeek.FRIDAY to "FRI",
            java.time.DayOfWeek.SATURDAY to "SAT",
            java.time.DayOfWeek.SUNDAY to "SUN"
        )
        val currentDayKey = dayMap[currentDay] ?: return false
        if (!daysOfWeek.split(",").contains(currentDayKey)) return false

        return if (start <= end) {
            now in start..end
        } else {
            now >= start || now <= end
        }
    }

    private fun goHome() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun showTimer(packageName: String) {
        if (museApp.isTimerShowing) return
        if (timedApps.contains(packageName)) return
        museApp.isTimerShowing = true
        timedApps.add(packageName)

        val timerDuration = listOf(20,40, 60).random()

        scope.launch(Dispatchers.Main) {
            val params = android.view.WindowManager.LayoutParams(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                android.graphics.PixelFormat.TRANSLUCENT
            )

            val composeView = androidx.compose.ui.platform.ComposeView(this@BlockingService).apply {
                setViewTreeLifecycleOwner(overlayLifecycleOwner)
                setViewTreeSavedStateRegistryOwner(overlayLifecycleOwner)
                setContent {
                    TimerOverlay(
                        duration = timerDuration,
                        onFinished = { removeOverlay() }
                    )
                }
            }

            overlayView = composeView
            windowManager.addView(composeView, params)
        }
    }

    private fun removeOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
        museApp.isTimerShowing = false
    }

    @androidx.compose.runtime.Composable
    private fun TimerOverlay(duration: Int, onFinished: () -> Unit) {
        var secondsLeft by androidx.compose.runtime.remember {
            androidx.compose.runtime.mutableIntStateOf(duration)
        }

        androidx.compose.runtime.LaunchedEffect(Unit) {
            while (secondsLeft > 0) {
                kotlinx.coroutines.delay(1000)
                secondsLeft--
            }
            onFinished()
        }

        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Text(
                    text = "$secondsLeft",
                    fontSize = 96.sp,
                    color = androidx.compose.ui.graphics.Color.White
                )
                androidx.compose.material3.Text(
                    text = "seconds",
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }

    private fun isLauncher(packageName: String): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val pm = applicationContext.packageManager
        return pm.queryIntentActivities(intent, 0).any { it.activityInfo.packageName == packageName }
    }

    private fun hasLauncherIntent(packageName: String): Boolean {
        val pm = applicationContext.packageManager
        return pm.getLaunchIntentForPackage(packageName) != null
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        overlayLifecycleOwner.onDestroy()
    }
}