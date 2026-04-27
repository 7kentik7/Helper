package com.example.helperjc

import androidx.compose.ui.graphics.Color
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.presentationJC.addEditPlan.AddPlanState
import com.example.helperjc.presentationJC.addEditTask.AddTaskState

fun AddPlanState.toDomain(): Plan =
    Plan(
        id = this.planId?.toInt() ?: Plan.UNDEFINED_ID,
        title = this.title.trim(),
        color = this.color,
        endTime = this.endTime?.parseToLocalDateTime()
    )

fun AddTaskState.toDomain(): Task =
    Task(
        id = this.id?.toInt() ?: Task.UNDEFINED_ID,
        title = this.title.trim(),
        description = this.description,
        isCompleted = this.isCompleted,
        priority = this.priority,
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