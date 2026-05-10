package com.lundjo.museapptimer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lundjo.museapptimer.data.model.App
import com.lundjo.museapptimer.data.model.Bundle
import com.lundjo.museapptimer.data.model.Schedule

@Database(
    entities = [Bundle::class, App::class, Schedule::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bundleDao(): BundleDao
    abstract fun appDao(): AppDao
    abstract fun scheduleDao(): ScheduleDao
}