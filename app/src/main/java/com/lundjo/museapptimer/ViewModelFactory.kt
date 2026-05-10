package com.lundjo.museapptimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lundjo.museapptimer.data.repository.BundleRepository
import com.lundjo.museapptimer.ui.bundle.BundleViewModel
import com.lundjo.museapptimer.ui.schedule.TimeAndBundlesViewModel
import com.lundjo.museapptimer.ui.settings.SettingsViewModel
import com.lundjo.museapptimer.data.SettingsDataStore

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BundleRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BundleViewModel::class.java) -> BundleViewModel(repository) as T
            modelClass.isAssignableFrom(TimeAndBundlesViewModel::class.java) -> TimeAndBundlesViewModel(repository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settingsDataStore) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}