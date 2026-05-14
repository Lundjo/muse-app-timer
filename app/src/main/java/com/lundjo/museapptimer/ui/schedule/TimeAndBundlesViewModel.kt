package com.lundjo.museapptimer.ui.schedule

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lundjo.museapptimer.data.model.App
import com.lundjo.museapptimer.data.model.Bundle
import com.lundjo.museapptimer.data.model.Schedule
import com.lundjo.museapptimer.data.repository.BundleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun deleteBundle(bundle: Bundle) {
        viewModelScope.launch {
            repository.deleteBundle(bundle)
        }
    }

    fun getSchedulesForBundle(bundleId: Int): Flow<List<Schedule>> {
        return repository.getSchedulesForBundle(bundleId)
    }

    fun saveSchedule(bundleId: Int, startTime: String, endTime: String, daysOfWeek: String) {
        viewModelScope.launch {
            val existing = repository.getSchedulesForBundle(bundleId).first()
            if (existing.isEmpty()) {
                repository.insertSchedule(Schedule(
                    bundleId = bundleId,
                    startTime = startTime,
                    endTime = endTime,
                    daysOfWeek = daysOfWeek
                ))
            } else {
                repository.updateSchedule(existing.first().copy(
                    startTime = startTime,
                    endTime = endTime,
                    daysOfWeek = daysOfWeek
                ))
            }
        }
    }
}