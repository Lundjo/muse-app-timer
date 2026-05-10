package com.lundjo.museapptimer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private val EDITING_HOUR_KEY = intPreferencesKey("editing_hour")

    val editingHour: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[EDITING_HOUR_KEY] ?: 19
        }

    suspend fun saveEditingHour(hour: Int) {
        context.dataStore.edit { preferences ->
            preferences[EDITING_HOUR_KEY] = hour
        }
    }
}