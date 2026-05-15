package com.lundjo.museapptimer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lundjo.museapptimer.data.model.App
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM apps WHERE bundleId = :bundleId")
    fun getAppsForBundle(bundleId: Int): Flow<List<App>>

    @Insert
    suspend fun insert(app: App)

    @Delete
    suspend fun delete(app: App)

    @Query("SELECT packageName FROM apps")
    fun getBundledPackageNames(): Flow<List<String>>

    @Query("SELECT * FROM apps")
    fun getAllApps(): Flow<List<App>>
}