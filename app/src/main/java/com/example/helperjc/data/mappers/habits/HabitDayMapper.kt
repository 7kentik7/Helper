package com.example.helperjc.data.mappers.habits

import com.example.helperjc.data.local.database.models.HabitDayDbModel
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.utils.toEpochMonth
import com.example.helperjc.utils.toYearMonth
import javax.inject.Inject

class HabitDayMapper @Inject constructor() {
    fun mapEntityToModel(entity: HabitDay): HabitDayDbModel = HabitDayDbModel(
        id = entity.id,
        day = entity.day,
        period = entity.period.toEpochMonth(),
        isCompleted = entity.isCompleted
    )

    fun mapModelToEntity(model: HabitDayDbModel): HabitDay = HabitDay(
        id = model.id,
        day = model.day,
        period = model.period.toYearMonth(),
        isCompleted = model.isCompleted
    )

    fun mapListModelToListEntity(modelList: List<HabitDayDbModel>): List<HabitDay> {
        return modelList.map {
            mapModelToEntity(it)
        }
    }
}