package com.example.helperjc.domain.habbits

import java.time.LocalDate
import java.time.YearMonth

data class HabitDay(
    val id: Int,
    val habitId: Int,
    val date: LocalDate,
    val isCompleted: Boolean,
    val countOfRepetitions: Int,
    val countOfCompletedRepetitions: Int,
    val progress: Int
)