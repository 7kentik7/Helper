package com.example.helperjc.data.local.repositoriesimpl

import com.example.helperjc.data.local.database.PlanDao
import com.example.helperjc.data.local.mappers.PlanDetailsMapper
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plandetails.repository.PlanDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlanDetailsRepositoryImpl @Inject constructor(
    private val planDao: PlanDao, private val mapper: PlanDetailsMapper
) : PlanDetailsRepository {
    override fun getPlanDetails(planId: Int): Flow<PlanDetails> {
        return planDao.getFlowPlanDetails(planId = planId).map {
            mapper.mapModelToEntity(it)
        }
    }

    override fun getListPlanDetails(): Flow<List<PlanDetails>> {
        return planDao.getFlowListPlanWithTasks().map {
            mapper.mapModelListToListEntity(it)
        }
    }
}