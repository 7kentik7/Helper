package com.example.helperjc.domain.plandetails

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task

data class PlanDetails(
    val plan: Plan,
    val tasks: List<Task> = listOf(),
    val progress: Int = 0,
    val countOfTasks: Int = 0,
    val countOfCompletedTasks: Int = 0
) {
}