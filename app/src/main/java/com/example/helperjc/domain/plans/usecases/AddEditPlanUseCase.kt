package com.example.helperjc.domain.plans.usecases

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.repository.PlanRepository
import javax.inject.Inject

class AddEditPlanUseCase @Inject constructor(
    private val repository: PlanRepository
) {
    suspend operator fun invoke(plan: Plan) {
        repository.addEditPlan(plan)
    }
}