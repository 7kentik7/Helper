package com.example.helperjc.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class PlanDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val repeatAt: String,
    val startTime: Long,
    val endTime: Long?,
    val color: String
)