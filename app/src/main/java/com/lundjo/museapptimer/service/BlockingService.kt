package com.lundjo.museapptimer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
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

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return
        if (packageName == this.packageName) return

        scope.launch {
            val app = applicationContext as MuseApp
            val apps = app.database.appDao().getAllApps().first()
            val blockedApp = apps.find { it.packageName == packageName } ?: return@launch
            val schedule = app.database.scheduleDao()
                .getSchedulesForBundle(blockedApp.bundleId).first()
                .firstOrNull() ?: return@launch

            if (isCurrentlyBlocked(schedule.startTime, schedule.endTime, schedule.daysOfWeek)) {
                goHome()
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

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}