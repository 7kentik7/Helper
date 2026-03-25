package com.example.helperjc.presentation.states.tasks

import androidx.annotation.StringRes
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority

sealed class AddTaskForPlanState {
    data object Initial : AddTaskForPlanState()
    data object Success : AddTaskForPlanState()
    data class Error(@param:StringRes val messageResId: Int) : AddTaskForPlanState()
    data class DataLoaded(
        val id: Int = Task.UNDEFINED_ID,
        val title: String = "",
        val description: String = "",
        val isActive: Boolean = true,
        val priority: TaskPriority = TaskPriority.MEDIUM,
        val planId: Int = Plan.UNDEFINED_ID
    ) : AddTaskForPlanState()

}