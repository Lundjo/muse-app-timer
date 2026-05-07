package com.lundjo.museapptimer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lundjo.museapptimer.ui.home.HomeScreen
import com.lundjo.museapptimer.ui.bundle.CreateBundleScreen
import com.lundjo.museapptimer.ui.schedule.TimeAndBundlesScreen
import com.lundjo.museapptimer.ui.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onBundlesClick = { navController.navigate("createBundle") },
                onScheduleClick = { navController.navigate("schedule") },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("createBundle") {
            CreateBundleScreen()
        }
        composable("schedule") {
            TimeAndBundlesScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}