package com.lundjo.museapptimer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lundjo.museapptimer.data.repository.BundleRepository
import com.lundjo.museapptimer.ui.bundle.BundleViewModel
import com.lundjo.museapptimer.ui.schedule.TimeAndBundlesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BundleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BundleViewModel::class.java) -> BundleViewModel(repository) as T
            modelClass.isAssignableFrom(TimeAndBundlesViewModel::class.java) -> TimeAndBundlesViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}