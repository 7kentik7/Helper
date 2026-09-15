package com.example.helperjc.data.local.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class HabitWithDaysModel(
    @Embedded
    val habit: HabitDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val habitDays: List<HabitDayDbModel>
)