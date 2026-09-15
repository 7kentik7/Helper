package com.example.helperjc.domain.habbits

import java.time.YearMonth

data class HabitDay(
    val id: Int,
    val day: Int,
    val period: YearMonth,
    val isCompleted: Boolean
)