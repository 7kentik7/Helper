package com.example.helperjc.presentation.states.plans

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task

sealed class PlanDetailsState {
    data class DataLoaded(
        val plan: Plan = Plan(),
        val tasksList: List<Task> = listOf(),
        val progress: Int = 0
    ) : PlanDetailsState()

    data object Initial : PlanDetailsState()
    data object Success: PlanDetailsState()
}