package com.example.helperjc.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.helperjc.data.database.models.TaskDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE planId IS null")
    fun getTasksFLow(): Flow<List<TaskDbModel>>

    @Upsert
    suspend fun addEditTask(task: TaskDbModel)

    @Query("DELETE FROM tasks WHERE id=:taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTask(taskId: Int): TaskDbModel?
}