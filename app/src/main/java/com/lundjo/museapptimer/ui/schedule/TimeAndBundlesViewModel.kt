package com.lundjo.museapptimer.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lundjo.museapptimer.data.model.App
import com.lundjo.museapptimer.data.repository.BundleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TimeAndBundlesViewModel(private val repository: BundleRepository) : ViewModel() {

    val bundles = repository.getAllBundles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getAppsForBundle(bundleId: Int): Flow<List<App>> {
        return repository.getAppsForBundle(bundleId)
    }
}