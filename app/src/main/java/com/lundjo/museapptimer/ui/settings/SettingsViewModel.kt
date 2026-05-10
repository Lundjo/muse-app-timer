package com.lundjo.museapptimer.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lundjo.museapptimer.data.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    val editingHour = settingsDataStore.editingHour
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 19
        )

    fun saveEditingHour(hour: Int) {
        viewModelScope.launch {
            settingsDataStore.saveEditingHour(hour)
        }
    }
}