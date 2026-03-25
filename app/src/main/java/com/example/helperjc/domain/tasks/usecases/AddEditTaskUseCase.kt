package com.example.helperjc.domain.tasks.usecases


import com.example.helperjc.domain.tasks.repository.TaskRepository
import com.example.helperjc.domain.tasks.Task
import javax.inject.Inject

class AddEditTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.addEditTask(task)
    }
}