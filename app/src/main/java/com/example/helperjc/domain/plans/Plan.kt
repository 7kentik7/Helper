package com.example.helperjc.domain.plans

import androidx.compose.ui.graphics.Color
import com.example.helperjc.enums.PlanRepeatType
import java.time.LocalDateTime

data class Plan(
    val id: Int = UNDEFINED_ID,
    val title: String = "",
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val repeatAt: PlanRepeatType = PlanRepeatType.NONE,
    val color: Color = Color.Gray
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}

