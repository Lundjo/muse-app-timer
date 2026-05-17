package com.lundjo.museapptimer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lundjo.museapptimer.MuseApp
import com.lundjo.museapptimer.ViewModelFactory
import com.lundjo.museapptimer.ui.bundle.BundleViewModel
import com.lundjo.museapptimer.ui.bundle.CreateBundleScreen
import com.lundjo.museapptimer.ui.home.HomeScreen
import com.lundjo.museapptimer.ui.schedule.TimeAndBundlesScreen
import com.lundjo.museapptimer.ui.schedule.TimeAndBundlesViewModel
import com.lundjo.museapptimer.ui.settings.SettingsScreen
import androidx.compose.ui.platform.LocalContext
import com.lundjo.museapptimer.ui.home.AboutScreen
import com.lundjo.museapptimer.ui.schedule.BundleDetailScreen
import com.lundjo.museapptimer.ui.settings.SettingsViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as MuseApp
    val factory = ViewModelFactory(app.repository, app.settingsDataStore)

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onBundlesClick = { navController.navigate("createBundle") },
                onScheduleClick = { navController.navigate("schedule") },
                onSettingsClick = { navController.navigate("settings") },
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("createBundle") {
            val viewModel: BundleViewModel = viewModel(factory = factory)
            CreateBundleScreen(viewModel = viewModel)
        }
        composable("schedule") {
            val viewModel: TimeAndBundlesViewModel = viewModel(factory = factory)
            TimeAndBundlesScreen(
                viewModel = viewModel,
                onBundleClick = { bundleId -> navController.navigate("bundleDetail/$bundleId") }
            )
        }
        composable("settings") {
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(viewModel = viewModel)
        }
        composable("bundleDetail/{bundleId}") { backStackEntry ->
            val bundleId = backStackEntry.arguments?.getString("bundleId")?.toInt() ?: return@composable
            val viewModel: TimeAndBundlesViewModel = viewModel(factory = factory)
            BundleDetailScreen(
                bundleId = bundleId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("about") {
            AboutScreen()
        }
    }
}