package com.example.helperjc.data.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tittle: String,
    val
)