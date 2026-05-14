package com.lundjo.museapptimer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [ForeignKey(
        entity = Bundle::class,
        parentColumns = ["id"],
        childColumns = ["bundleId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("bundleId")]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bundleId: Int,
    val startTime: String,
    val endTime: String,
    val daysOfWeek: String
)