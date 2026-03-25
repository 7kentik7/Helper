package com.example.helperjc.data.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks", foreignKeys = [
        ForeignKey(
            entity = PlanDbModel::class,
            parentColumns = ["id"],
            childColumns = ["planId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("planId")]
)
data class TaskDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String?,
    val isActive: Boolean,
    val priority: String,
    val planId: Int?
)