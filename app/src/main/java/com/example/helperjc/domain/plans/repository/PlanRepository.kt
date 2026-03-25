package com.example.helperjc.domain.plans.repository

import com.example.helperjc.domain.plans.Plan

interface PlanRepository {
    suspend fun addEditPlan(plan: Plan)
    suspend fun deletePlan(planId: Int)
    suspend fun getPlan(planId: Int): Plan?
}