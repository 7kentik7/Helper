package com.example.helperjc.domain.habbits.usecases

import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.domain.habbits.repository.HabitWithDaysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitWithDaysListUseCase @Inject constructor(
    val repository: HabitWithDaysRepository
) {
    operator fun invoke(): Flow<List<HabitWithDays>> {
        return repository.getListHabitWithDays()
    }
}