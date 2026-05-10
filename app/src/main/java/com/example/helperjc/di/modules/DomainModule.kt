package com.example.helperjc.di.modules

import com.example.helperjc.data.local.repositoriesimpl.PlanDetailsRepositoryImpl
import com.example.helperjc.data.local.repositoriesimpl.PlanRepositoryImpl
import com.example.helperjc.data.local.repositoriesimpl.TaskRepositoryImpl
import com.example.helperjc.domain.plandetails.repository.PlanDetailsRepository
import com.example.helperjc.domain.plans.repository.PlanRepository
import com.example.helperjc.domain.tasks.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
   abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPlanRepository(impl: PlanRepositoryImpl): PlanRepository

    @Binds
    @Singleton
    abstract  fun bindPlanDetailsRepository(impl: PlanDetailsRepositoryImpl): PlanDetailsRepository
}