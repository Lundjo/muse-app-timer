package com.lundjo.museapptimer

import android.app.Application
import androidx.room.Room
import com.lundjo.museapptimer.data.db.AppDatabase
import com.lundjo.museapptimer.data.repository.BundleRepository

class MuseApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "muse_database"
        ).build()
    }

    val repository by lazy {
        BundleRepository(database)
    }
}