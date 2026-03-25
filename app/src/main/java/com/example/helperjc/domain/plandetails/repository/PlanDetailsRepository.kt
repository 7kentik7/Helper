package com.example.helperjc.domain.plandetails.repository

import com.example.helperjc.domain.plandetails.PlanDetails
import kotlinx.coroutines.flow.Flow

interface PlanDetailsRepository {
    fun getPlanDetails(planId: Int): Flow<PlanDetails>
    fun getListPlanDetails(): Flow<List<PlanDetails>>
}