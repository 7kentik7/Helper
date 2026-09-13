package com.example.helperjc.domain.habbits.usecases

import com.example.helperjc.domain.habbits.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habitId: Int) {
        repository.deleteHabit(habitId)
    }
}