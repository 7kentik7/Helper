package com.example.helperjc.domain.tasks

import com.example.helperjc.enums.TaskPriority
import javax.inject.Inject

data class Task @Inject constructor(
    val id: Int = UNDEFINED_ID,
    val title: String = "",
    val description: String? = null,
    val isActive: Boolean = true,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val planId: Int? = null
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}