package com.lundjo.museapptimer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lundjo.museapptimer.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE bundleId = :bundleId")
    fun getSchedulesForBundle(bundleId: Int): Flow<List<Schedule>>

    @Insert
    suspend fun insert(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Update
    suspend fun update(schedule: Schedule)
}