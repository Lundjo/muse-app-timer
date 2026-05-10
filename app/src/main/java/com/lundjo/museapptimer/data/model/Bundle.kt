package com.lundjo.museapptimer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bundles")
data class Bundle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)