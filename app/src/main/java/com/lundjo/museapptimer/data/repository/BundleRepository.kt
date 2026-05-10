package com.lundjo.museapptimer.data.repository

import com.lundjo.museapptimer.data.db.AppDatabase
import com.lundjo.museapptimer.data.model.Bundle
import kotlinx.coroutines.flow.Flow

class BundleRepository(private val database: AppDatabase) {

    fun getAllBundles(): Flow<List<Bundle>> {
        return database.bundleDao().getAll()
    }

    suspend fun insertBundle(bundle: Bundle) {
        database.bundleDao().insert(bundle)
    }

    suspend fun deleteBundle(bundle: Bundle) {
        database.bundleDao().delete(bundle)
    }
}