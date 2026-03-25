package com.example.helperjc.presentation.states.plans

import androidx.annotation.StringRes
import com.example.helperjc.domain.plans.Plan.Companion.UNDEFINED_ID
import com.example.helperjc.enums.PlanColor
import com.example.helperjc.enums.PlanRepeatType
import java.time.LocalDateTime

sealed class EditPlanDetailsState {
    data class DataLoaded(
        val id: Int = UNDEFINED_ID,
        val title: String = "",
        val color: PlanColor = PlanColor.Default,
        val startTime: LocalDateTime = LocalDateTime.now(),
        val endTime: LocalDateTime? = null,
        val repeatAt: PlanRepeatType = PlanRepeatType.NONE,
    ) : EditPlanDetailsState()

    data object Initial : EditPlanDetailsState()
    data object Success : EditPlanDetailsState()
    data class Error(@param:StringRes val messageResId: Int) : EditPlanDetailsState()
}