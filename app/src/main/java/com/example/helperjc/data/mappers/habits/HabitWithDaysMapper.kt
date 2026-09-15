package com.example.helperjc.data.mappers.habits

import com.example.helperjc.data.local.database.models.HabitWithDaysModel
import com.example.helperjc.domain.habbits.HabitWithDays
import javax.inject.Inject

class HabitWithDaysMapper @Inject constructor(
    val habitMapper: HabitMapper,
    val habitDayMapper: HabitDayMapper
) {
    fun mapModelToEntity(model: HabitWithDaysModel): HabitWithDays {
        val habitEntity = habitMapper.mapModelToEntity(model.habit)
        val habitDaysEntity = habitDayMapper.mapListModelToListEntity(model.habitDays)
        return HabitWithDays(habit = habitEntity, days = habitDaysEntity)
    }

    fun mapModelListToListEntity(modelList: List<HabitWithDaysModel>): List<HabitWithDays> {
        return modelList.map {
            mapModelToEntity(it)
        }
    }
}