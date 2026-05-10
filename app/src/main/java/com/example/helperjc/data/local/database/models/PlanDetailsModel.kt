package com.example.helperjc.data.local.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class PlanDetailsModel(
    @Embedded
    val plan: PlanDbModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "planId"
    )
    val tasks: List<TaskDbModel>
)