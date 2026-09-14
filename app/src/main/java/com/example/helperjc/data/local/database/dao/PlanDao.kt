package com.example.helperjc.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.helperjc.data.local.database.models.PlanDbModel
import com.example.helperjc.data.local.database.models.PlanDetailsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Upsert
    suspend fun addEditPlan(plan: PlanDbModel)

    @Query("DELETE FROM plans WHERE id=:planId")
    suspend fun deletePlan(planId: Int)

    @Query("SELECT * FROM plans WHERE id = :planId LIMIT 1")
    suspend fun getPlan(planId: Int): PlanDbModel?

    @Transaction
    @Query("SELECT * FROM plans WHERE id = :planId")
    fun getFlowPlanDetails(planId: Int): Flow<PlanDetailsModel>

    @Transaction
    @Query("SELECT * FROM plans")
    fun getFlowListPlanWithTasks(): Flow<List<PlanDetailsModel>>

    @Insert
    suspend fun addAiPlan(plan: PlanDbModel): Long
}