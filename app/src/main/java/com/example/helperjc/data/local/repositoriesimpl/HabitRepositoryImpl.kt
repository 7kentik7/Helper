package com.example.helperjc.data.local.repositoriesimpl

import com.example.helperjc.data.local.database.dao.HabitDao
import com.example.helperjc.data.local.database.dao.HabitDayDao
import com.example.helperjc.data.mappers.habits.HabitDayMapper
import com.example.helperjc.data.mappers.habits.HabitMapper
import com.example.helperjc.data.mappers.habits.HabitWithDaysMapper
import com.example.helperjc.domain.habbits.Habit
import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.domain.habbits.repository.HabitRepository
import com.example.helperjc.domain.habbits.repository.HabitWithDaysRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitDayDao: HabitDayDao,
    private val habitMapper: HabitMapper,
    private val habitDayMapper: HabitDayMapper,
    private val habitWithDaysMapper: HabitWithDaysMapper
) : HabitRepository, HabitWithDaysRepository {
    override suspend fun getHabit(habitId: Int): Habit? {
        return habitDao.getHabit(habitId)?.let {
            habitMapper.mapModelToEntity(it)
        }
    }

    override suspend fun addEditHabit(habit: Habit) {
        habitDao.addEditHabit(habitMapper.mapEntityToModel(habit))
    }

    override suspend fun deleteHabit(habitId: Int) {
        habitDao.deleteHabit(habitId)
    }

    override fun getHabitWithDays(habitId: Int): Flow<HabitWithDays> {
        return habitDao.getHabitWithDaysFlow(habitId = habitId).map {
            habitWithDaysMapper.mapModelToEntity(it)
        }
    }

    override fun getListHabitWithDays(): Flow<List<HabitWithDays>> {
        return habitDao.getHabitsList().map {
            habitWithDaysMapper.mapModelListToListEntity(it)
        }
    }
}