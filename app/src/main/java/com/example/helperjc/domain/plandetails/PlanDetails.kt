package com.example.helperjc.domain.plandetails

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task

data class PlanDetails(
    val plan: Plan,
    val tasks: List<Task>,
    val progress: Int,
    val countOfTasks: Int,
    val countOfCompletedTasks: Int
) {
}