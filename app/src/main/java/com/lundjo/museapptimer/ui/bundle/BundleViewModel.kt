package com.lundjo.museapptimer.ui.bundle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lundjo.museapptimer.data.model.App
import com.lundjo.museapptimer.data.model.Bundle
import com.lundjo.museapptimer.data.repository.BundleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BundleViewModel(private val repository: BundleRepository) : ViewModel() {

    val bundles = repository.getAllBundles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bundledPackageNames = repository.getBundledPackageNames()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteBundle(bundle: Bundle) {
        viewModelScope.launch {
            repository.deleteBundle(bundle)
        }
    }

    fun createBundle(name: String, selectedPackages: Set<String>, allApps: List<App>) {
        viewModelScope.launch {
            val bundle = Bundle(name = name)
            val bundleId = repository.insertBundleAndGetId(bundle)
            selectedPackages.forEach { packageName ->
                val app = allApps.first { it.packageName == packageName }
                repository.insertApp(App(
                    bundleId = bundleId.toInt(),
                    packageName = app.packageName,
                    displayName = app.displayName
                ))
            }
        }
    }
}