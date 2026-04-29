package com.example.helperjc.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.helperjc.data.database.models.PlanDbModel
import com.example.helperjc.data.database.models.TaskDbModel

@Database(entities = [TaskDbModel::class, PlanDbModel::class], version = 12, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun planDao(): PlanDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private val DB_NAME = "my_helper.db"

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
            }
            val db = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
            INSTANCE = db
            return db
        }

    }
}