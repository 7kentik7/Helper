package com.example.helperjc.domain.habbits

import java.time.YearMonth


data class Habit(
    val id: Int = UNDEFINED_ID,
    val title: String = "",
    val month: YearMonth = YearMonth.now(),
    val habitDaysList: List<HabitDay> = listOf(),
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}