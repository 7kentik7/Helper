package com.example.helperjc.data.mappers.habits

import com.example.helperjc.data.local.database.models.HabitDbModel
import com.example.helperjc.domain.habbits.Habit
import javax.inject.Inject

class HabitMapper @Inject constructor() {
    fun mapEntityToModel(entity: Habit): HabitDbModel =
        HabitDbModel(id = entity.id, tittle = entity.title)

    fun mapModelToEntity(model: HabitDbModel): Habit =
        Habit(id = model.id, title = model.tittle)
}