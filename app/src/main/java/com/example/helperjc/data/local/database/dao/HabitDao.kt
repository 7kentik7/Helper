package com.example.helperjc.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.helperjc.data.local.database.models.HabitDbModel
import com.example.helperjc.domain.habbits.Habit

@Dao
interface HabitDao {
    @Upsert
    suspend fun addEditHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Int)

    @Query("SELECT * FROM habits WHERE id=:habitId LIMIT 1 ")
    suspend fun getHabit(habitId: Int): HabitDbModel?

}