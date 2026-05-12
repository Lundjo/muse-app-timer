package com.lundjo.museapptimer.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import com.lundjo.museapptimer.data.model.App

@SuppressLint("QueryPermissionsNeeded")
fun getInstalledApps(context: Context): List<App> {
    val pm = context.packageManager
    val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
    return apps
        .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
        .map { appInfo ->
            App(
                bundleId = 0,
                packageName = appInfo.packageName,
                displayName = pm.getApplicationLabel(appInfo).toString()
            )
        }
        .sortedBy { it.displayName }
}