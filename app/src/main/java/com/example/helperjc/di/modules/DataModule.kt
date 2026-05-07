package com.example.helperjc.di.modules

import android.app.Application
import android.content.Context
import com.example.helperjc.data.database.AppDatabase
import com.example.helperjc.data.database.PlanDao
import com.example.helperjc.data.database.TaskDao
import com.example.helperjc.notifications.NotificationScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideNotificationScheduler(@ApplicationContext context: Context): NotificationScheduler =
        NotificationScheduler(context)

}