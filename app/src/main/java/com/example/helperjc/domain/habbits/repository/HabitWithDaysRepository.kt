package com.example.helperjc.domain.habbits.repository

import com.example.helperjc.domain.habbits.HabitWithDays
import kotlinx.coroutines.flow.Flow

interface HabitWithDaysRepository {
    fun getHabitWithDays(habitId: Int): Flow<HabitWithDays>
    fun getListHabitWithDays(): Flow<List<HabitWithDays>>
}