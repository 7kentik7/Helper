package com.example.helperjc.data.local.repositoriesimpl

import com.example.helperjc.data.local.database.dao.PlanDao
import com.example.helperjc.data.mappers.plans.PlanMapper
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.repository.PlanRepository
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val planDao: PlanDao,
    private val mapper: PlanMapper
) : PlanRepository {
    override suspend fun addEditPlan(plan: Plan) {
        planDao.addEditPlan(mapper.mapEntityToModel(plan))
    }

    override suspend fun deletePlan(planId: Int) {
        planDao.deletePlan(planId)
    }

    override suspend fun getPlan(planId: Int): Plan? {
        return planDao.getPlan(planId)?.let {
            mapper.mapModelToEntity(it)
        }
    }
}