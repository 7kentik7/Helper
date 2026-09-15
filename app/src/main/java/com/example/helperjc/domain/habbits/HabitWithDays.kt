package com.example.helperjc.domain.habbits

data class HabitWithDays(
    val habit: Habit,
    val days: List<HabitDay>
)