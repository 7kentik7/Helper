package com.example.helperjc.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.helperjc.data.local.database.models.PlanDbModel
import com.example.helperjc.data.local.database.models.TaskDbModel
import com.example.helperjc.data.network.PlanDto
import com.example.helperjc.data.network.TaskDto
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentationJC.addEditPlan.AddPlanState
import com.example.helperjc.presentationJC.addEditTask.AddTaskState
import java.time.LocalDateTime

fun AddPlanState.toDomain(): Plan =
    Plan(
        id = this.planId?.toInt() ?: Plan.UNDEFINED_ID,
        title = this.title.trim(),
        color = this.color,
        endTime = this.endTime?.parseToLocalDateTime(),
        repeatAt = this.repeatType,
        startTime = this.startTime ?: LocalDateTime.now()
    )

fun AddTaskState.toDomain(): Task =
    Task(
        id = this.id?.toInt() ?: Task.UNDEFINED_ID,
        title = this.title.trim(),
        description = this.description,
        isCompleted = this.isCompleted,
        priority = this.priority,
        planId = this.planId?.toIntOrNull()
    )

fun Color.muted(amount: Float = 0.35f): Color {
    val gray = Color.Gray
    return Color(
        red = red * (1 - amount) + gray.red * amount,
        green = green * (1 - amount) + gray.green * amount,
        blue = blue * (1 - amount) + gray.blue * amount,
        alpha = 1f
    )
}

fun Color.toHex(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    val a = (alpha * 255).toInt()
    return String.format("#%02X%02X%02X%02X", a, r, g, b)
}

fun String.toColor(): Color {
    return Color(this.toColorInt())
}