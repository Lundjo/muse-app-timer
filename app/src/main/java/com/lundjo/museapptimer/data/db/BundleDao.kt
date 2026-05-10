package com.lundjo.museapptimer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lundjo.museapptimer.data.model.Bundle
import kotlinx.coroutines.flow.Flow

@Dao
interface BundleDao {
    @Query("SELECT * FROM bundles")
    fun getAll(): Flow<List<Bundle>>

    @Insert
    suspend fun insert(bundle: Bundle)

    @Delete
    suspend fun delete(bundle: Bundle)
}