package com.example.helperjc.data.mappers.habits

import com.example.helperjc.data.local.database.models.HabitDbModel
import com.example.helperjc.domain.habbits.Habit
import com.example.helperjc.utils.toColor
import com.example.helperjc.utils.toHex
import javax.inject.Inject

class HabitMapper @Inject constructor() {
    fun mapEntityToModel(entity: Habit): HabitDbModel =
        HabitDbModel(id = entity.id, title = entity.title, color = entity.color.toHex())

    fun mapModelToEntity(model: HabitDbModel): Habit =
        Habit(id = model.id, title = model.title, color = model.color.toColor())
}