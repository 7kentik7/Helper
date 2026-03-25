package com.example.helperjc.data.mappers

import com.example.helperjc.data.database.models.PlanDbModel
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.enums.PlanColor
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperapp.toLocalDateTime
import com.example.helperapp.toTimeInMillis
import javax.inject.Inject

class PlanMapper @Inject constructor() {
    fun mapEntityToModel(entity: Plan): PlanDbModel = PlanDbModel(
        id = entity.id,
        title = entity.title,
        startTime = entity.startTime.toTimeInMillis(),
        endTime = entity.endTime?.toTimeInMillis(),
        repeatAt = entity.repeatAt.name,
        color = entity.color.name
    )

    fun mapModelToEntity(model: PlanDbModel): Plan = Plan(
        id = model.id,
        title = model.title,
        startTime = model.startTime.toLocalDateTime(),
        endTime = model.endTime?.toLocalDateTime(),
        repeatAt = PlanRepeatType.valueOf(model.repeatAt),
        color = PlanColor.valueOf(model.color)
    )
}