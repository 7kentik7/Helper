package com.example.helperjc.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.helperjc.domain.habbits.HabitDay

@Dao
interface HabitDayDao {

    @Upsert
    suspend fun addEditHabitDay(habitDay: HabitDay)

    @Query("DELETE FROM habitDays WHERE id = :habitDayId")
    suspend fun deleteHabit(habitDayId: Int)
}