package com.example.helperjc.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.helperjc.data.local.database.models.HabitDayDbModel
import com.example.helperjc.domain.habbits.HabitDay

@Dao
interface HabitDayDao {
    @Upsert
    suspend fun addEditHabitDay(habitDay: HabitDayDbModel)
}