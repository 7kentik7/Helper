package com.example.helperjc.data.mappers

import androidx.compose.ui.graphics.Color
import com.example.helperjc.data.local.database.models.PlanDbModel
import com.example.helperjc.data.local.database.models.TaskDbModel
import com.example.helperjc.data.network.PlanDto
import com.example.helperjc.data.network.TaskDto
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.utils.toHex
import com.example.helperjc.utils.toTimeInMillis
import java.time.LocalDateTime
import javax.inject.Inject

class PlanDtoMapper @Inject constructor() {
    fun toPlanDbModel(planDto: PlanDto): PlanDbModel {
        return PlanDbModel(
            id = 0,
            title = planDto.title,
            color = Color.Gray.toHex(),
            repeatAt = PlanRepeatType.NONE.name,
            startTime = LocalDateTime.now().toTimeInMillis(),
            endTime = null
        )
    }

    fun toTaskDbModels(tasks: List<TaskDto>, planId: Int): List<TaskDbModel> {
        return tasks.map { task -> toTaskDbModel(task, planId) }
    }

    private fun toTaskDbModel(taskDto: TaskDto, planId: Int): TaskDbModel {
        return TaskDbModel(
            id = 0,
            title = taskDto.title,
            description = taskDto.description,
            isActive = false,
            priority = parsePriority(taskDto.priority).name,
            planId = planId
        )
    }
    private fun parsePriority(priority: String): TaskPriority {
        return try {
            TaskPriority.valueOf(priority.uppercase())
        } catch (e: IllegalArgumentException) {
            TaskPriority.MEDIUM
        }
    }
}