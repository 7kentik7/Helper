package com.example.helperjc.data.mappers

import com.example.helperjc.data.database.models.PlanDetailsModel
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import javax.inject.Inject

class PlanDetailsMapper @Inject constructor(
    val planMapper: PlanMapper,
    val taskMapper: TaskMapper
) {
    fun mapModelToEntity(model: PlanDetailsModel): PlanDetails {
        val planEntity = planMapper.mapModelToEntity(model.plan)
        val taskListEntity = taskMapper.mapListModelToListEntity(model.tasks)
        val progress = countProgress(taskListEntity)
        val countOfTasks = taskListEntity.count()
        val countOfCompletedTasks = taskListEntity.count { !it.isActive }
        return PlanDetails(
            planEntity,
            taskListEntity,
            progress,
            countOfTasks,
            countOfCompletedTasks
        )
    }

    fun mapModelListToListEntity(modelList: List<PlanDetailsModel>): List<PlanDetails> {
        return modelList.map {
            mapModelToEntity(it)
        }
    }

    private fun countProgress(tasks: List<Task>): Int {
        val totalWeight = tasks.sumOf { getWeight(it.priority) }
        val completedWeight = tasks.filter { !it.isActive }.sumOf { getWeight(it.priority) }
        return if (totalWeight != 0) {
            ((completedWeight.toDouble() / totalWeight) * 100).toInt()
        } else {
            0
        }
    }

    private fun getWeight(taskPriority: TaskPriority): Int {
        return when (taskPriority) {
            TaskPriority.LOW -> 1
            TaskPriority.MEDIUM -> 2
            TaskPriority.HIGH -> 3
        }
    }
}