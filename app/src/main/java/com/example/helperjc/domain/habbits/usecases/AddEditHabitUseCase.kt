package com.example.helperjc.domain.habbits.usecases

import com.example.helperjc.domain.habbits.Habit
import com.example.helperjc.domain.habbits.repository.HabitRepository
import javax.inject.Inject

class AddEditHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit) {
        repository.addEditHabit(habit)
    }
}