package com.example.helperjc.data.local.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(
    tableName = "habit_days",
    primaryKeys = ["habitId", "date"],
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
    val habitId: Int,
    val date: Long,
    val isCompleted: Boolean,
    val countOfRepetitions: Int,
    val countOfCompletedRepetitions: Int
)