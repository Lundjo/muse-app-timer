package com.lundjo.museapptimer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps",
    foreignKeys = [ForeignKey(
        entity = Bundle::class,
        parentColumns = ["id"],
        childColumns = ["bundleId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class App(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bundleId: Int,
    val packageName: String,
    val displayName: String
)