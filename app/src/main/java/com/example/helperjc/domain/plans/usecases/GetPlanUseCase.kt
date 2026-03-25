package com.example.helperjc.domain.plans.usecases

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.repository.PlanRepository
import javax.inject.Inject

class GetPlanUseCase @Inject constructor(
    private val repository: PlanRepository
) {
    suspend operator fun invoke(planId: Int): Plan? {
        return repository.getPlan(planId)
    }
}