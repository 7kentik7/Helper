package com.example.helperjc.data.repositoriesimpl

import com.example.helperjc.data.database.TaskDao
import com.example.helperjc.data.mappers.TaskMapper
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val mapper: TaskMapper
) : TaskRepository {

    override fun getTasksFLow(): Flow<List<Task>> {
        return taskDao.getTasksFLow().map {
            mapper.mapListModelToListEntity(it)
        }
    }

    override suspend fun addEditTask(task: Task) {
        return taskDao.addEditTask(
            mapper.mapEntityToModel(task)
        )
    }

    override suspend fun deleteTask(taskId: Int) {
        return taskDao.deleteTask(taskId)
    }

    override suspend fun getTask(taskId: Int): Task? {
        return taskDao.getTask(taskId)?.let {
            mapper.mapModelToEntity(it)
        }
    }
}