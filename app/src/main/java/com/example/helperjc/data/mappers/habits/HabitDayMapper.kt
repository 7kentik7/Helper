package com.example.helperjc.data.mappers.habits

import com.example.helperjc.data.local.database.models.HabitDayDbModel
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.utils.toEpochDayLong
import com.example.helperjc.utils.toLocalDate
import javax.inject.Inject

class HabitDayMapper @Inject constructor() {
    fun mapEntityToModel(entity: HabitDay): HabitDayDbModel = HabitDayDbModel(
        id = entity.id,
        habitId = entity.habitId,
        date = entity.date.toEpochDayLong(),
        isCompleted = entity.isCompleted,
        countOfRepetitions = entity.countOfRepetitions,
        countOfCompletedRepetitions = entity.countOfCompletedRepetitions,
    )

    fun mapModelToEntity(model: HabitDayDbModel): HabitDay = HabitDay(
        id = model.id,
        habitId = model.habitId,
        date = model.date.toLocalDate(),
        isCompleted = model.isCompleted,
        countOfRepetitions = model.countOfRepetitions,
        countOfCompletedRepetitions = model.countOfCompletedRepetitions,
        progress = calculateProgress(
            completed = model.countOfCompletedRepetitions,
            total = model.countOfRepetitions
        )
    )

    fun mapListModelToListEntity(modelList: List<HabitDayDbModel>): List<HabitDay> {
        return modelList.map {
            mapModelToEntity(it)
        }
    }

    private fun calculateProgress(completed: Int, total: Int): Int {
        if (total <= 0) return 0
        return (completed * 100) / total
    }
}