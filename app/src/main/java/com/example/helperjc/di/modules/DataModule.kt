package com.example.helperjc.di.modules

import android.app.Application
import com.example.helperjc.di.scopes.HelperScope
import com.example.helperjc.data.database.AppDatabase
import com.example.helperjc.data.database.PlanDao
import com.example.helperjc.data.database.TaskDao
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    companion object {
        @Provides
        @HelperScope
        fun provideTaskDao(application: Application): TaskDao =
            AppDatabase.getInstance(application).taskDao()

        @Provides
        @HelperScope
        fun providePlanDao(application: Application): PlanDao =
            AppDatabase.getInstance(application).planDao()
    }

}