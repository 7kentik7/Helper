package com.example.helperjc.domain.tasks.usecases


import com.example.helperjc.domain.tasks.repository.TaskRepository
import com.example.helperjc.domain.tasks.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksListUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasksFLow()
    }
}