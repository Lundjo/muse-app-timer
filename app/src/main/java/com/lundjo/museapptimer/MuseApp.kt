package com.lundjo.museapptimer

import android.app.Application
import androidx.room.Room
import com.lundjo.museapptimer.data.db.AppDatabase
import com.lundjo.museapptimer.data.repository.BundleRepository
import com.lundjo.museapptimer.data.SettingsDataStore
import kotlin.getValue

class MuseApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "muse_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    val repository by lazy {
        BundleRepository(database)
    }

    val settingsDataStore by lazy {
        SettingsDataStore(applicationContext)
    }

    var isTimerShowing = false
}