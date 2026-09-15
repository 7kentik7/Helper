package com.example.helperjc.domain.habbits.usecases

import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.domain.habbits.repository.HabitWithDaysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitWithDaysUseCase @Inject constructor(
    val repository: HabitWithDaysRepository
) {
    operator fun invoke(habitId: Int): Flow<HabitWithDays> {
        return repository.getHabitWithDays(habitId)
    }
}