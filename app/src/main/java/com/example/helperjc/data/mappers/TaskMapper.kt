package com.example.helperjc.data.mappers

import com.example.helperjc.data.local.database.models.TaskDbModel
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import javax.inject.Inject

class TaskMapper @Inject constructor() {
    fun mapEntityToModel(entity: Task): TaskDbModel = TaskDbModel(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        isActive = entity.isCompleted,
        priority = entity.priority.name,
        planId = entity.planId
    )

    fun mapModelToEntity(model: TaskDbModel): Task = Task(
        id = model.id,
        title = model.title,
        description = model.description,
        isCompleted = model.isActive,
        priority = TaskPriority.valueOf(model.priority),
        planId = model.planId
    )

    fun mapListModelToListEntity(modelsList: List<TaskDbModel>): List<Task> {
        return modelsList.map {
            mapModelToEntity(it)
        }
    }
}