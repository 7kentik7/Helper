package com.example.helperjc.presentation.states.tasks

import androidx.annotation.StringRes
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority

sealed class AddTaskState {
    data object Initial : AddTaskState()
    data object Success : AddTaskState()
    data class Error(@param:StringRes val messageResId: Int) : AddTaskState()
    data class DataLoaded(
        val id: Int = Task.UNDEFINED_ID,
        val title: String = "",
        val description: String = "",
        val isActive: Boolean = true,
        val priority: TaskPriority = TaskPriority.MEDIUM
    ) : AddTaskState()

}