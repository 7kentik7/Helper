package com.example.helperjc.domain.tasks.usecases

import com.example.helperjc.domain.tasks.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int) {
        repository.deleteTask(taskId)
    }
}