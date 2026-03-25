package com.example.helperjc.presentation.states.plans

import androidx.annotation.StringRes
import com.example.helperjc.enums.PlanColor
import java.time.LocalDateTime

sealed class AddPlanState {
    data object Success : AddPlanState()
    data class Error(@param:StringRes val messageResId: Int) : AddPlanState()
    data class Data(
        val title: String = "",
        val endTime: LocalDateTime? = null,
        val color: PlanColor = PlanColor.Default
    ) : AddPlanState()
}