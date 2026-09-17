package com.example.helperjc.data.local.database.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "habitDays",
    primaryKeys = ["habitId", "day", "period"],
    foreignKeys = [
        ForeignKey(
            entity = HabitDbModel::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitDayDbModel(
    val id: Int,
    val day: Int,
    val period: Long,
    val isCompleted: Boolean,
    val countOfRepetitions: Int,
    val countOfCompletedRepetitions: Int
)