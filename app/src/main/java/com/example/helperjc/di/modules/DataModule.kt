package com.example.helperjc.di

import android.app.Application
import com.example.helperjc.data.database.AppDatabase
import com.example.helperjc.data.database.PlanDao
import com.example.helperjc.data.database.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideTaskDao(application: Application): TaskDao =
        AppDatabase.getInstance(application).taskDao()

    @Singleton
    @Provides
    fun providePlanDao(application: Application): PlanDao =
        AppDatabase.getInstance(application).planDao()


}