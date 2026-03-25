package com.example.helperjc.domain.plandetails.usecases

import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plandetails.repository.PlanDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlanDetailsUseCase @Inject constructor(
    private val repository: PlanDetailsRepository
) {
    operator fun invoke(planId: Int): Flow<PlanDetails> {
        return repository.getPlanDetails(planId)
    }
}