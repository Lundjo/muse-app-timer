package com.lundjo.museapptimer.data.repository

import com.lundjo.museapptimer.data.db.AppDatabase
import com.lundjo.museapptimer.data.model.App
import com.lundjo.museapptimer.data.model.Bundle
import kotlinx.coroutines.flow.Flow

class BundleRepository(private val database: AppDatabase) {

    fun getAllBundles(): Flow<List<Bundle>> {
        return database.bundleDao().getAll()
    }

    suspend fun deleteBundle(bundle: Bundle) {
        database.bundleDao().delete(bundle)
    }

    suspend fun insertApp(app: App) {
        database.appDao().insert(app)
    }

    suspend fun insertBundleAndGetId(bundle: Bundle): Long {
        return database.bundleDao().insertAndGetId(bundle)
    }

    fun getBundledPackageNames(): Flow<List<String>> {
        return database.appDao().getBundledPackageNames()
    }
}