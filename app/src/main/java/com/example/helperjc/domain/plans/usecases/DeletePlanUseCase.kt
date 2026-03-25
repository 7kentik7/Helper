package com.example.helperjc.domain.plans.usecases

import com.example.helperjc.domain.plans.repository.PlanRepository
import javax.inject.Inject

class DeletePlanUseCase @Inject constructor(private val repository: PlanRepository) {
    suspend operator fun invoke(planId: Int) {
        repository.deletePlan(planId)
    }
}