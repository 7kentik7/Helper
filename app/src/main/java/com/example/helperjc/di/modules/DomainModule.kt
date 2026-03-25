package com.example.helperjc.di.modules

import com.example.helperjc.di.scopes.HelperScope
import com.example.helperjc.domain.plandetails.repository.PlanDetailsRepository
import com.example.helperjc.domain.plans.repository.PlanRepository
import com.example.helperjc.domain.tasks.repository.TaskRepository
import com.example.helperjc.data.repositoriesimpl.PlanDetailsRepositoryImpl
import com.example.helperjc.data.repositoriesimpl.PlanRepositoryImpl
import com.example.helperjc.data.repositoriesimpl.TaskRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {
    @Binds
    @HelperScope
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @HelperScope
    fun bindPlanRepository(impl: PlanRepositoryImpl): PlanRepository

    @Binds
    @HelperScope
    fun bindPlanDetailsRepository(impl: PlanDetailsRepositoryImpl): PlanDetailsRepository
}