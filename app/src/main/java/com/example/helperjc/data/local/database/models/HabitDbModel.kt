package com.example.helperjc.data.local.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.helperjc.domain.habbits.HabitDay

@Entity(tableName = "habits")
data class HabitDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tittle: String
)