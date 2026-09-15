package com.example.helperjc.domain.habbits.repository

import com.example.helperjc.domain.habbits.Habit

interface HabitRepository {
    suspend fun getHabit(habitId: Int): Habit?
    suspend fun addEditHabit(habit: Habit)
    suspend fun deleteHabit(habitId: Int)
}