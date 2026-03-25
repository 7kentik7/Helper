package com.example.helperjc.domain.tasks.usecases

import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.repository.TaskRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int): Task? {
        return repository.getTask(taskId)
    }
}