package com.example.helperjc.domain.tasks.repository

import com.example.helperjc.domain.tasks.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksFLow(): Flow<List<Task>>
    suspend fun addEditTask(task: Task)
    suspend fun deleteTask(taskId: Int)
    suspend fun getTask(taskId: Int): Task?

}