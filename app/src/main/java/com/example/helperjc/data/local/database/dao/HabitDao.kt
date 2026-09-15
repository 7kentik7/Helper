package com.example.helperjc.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.helperjc.data.local.database.models.HabitDbModel
import com.example.helperjc.data.local.database.models.HabitWithDaysModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Upsert
    suspend fun addEditHabit(habitDbModel: HabitDbModel)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Int)

    @Query("SELECT * FROM habits WHERE id=:habitId LIMIT 1 ")
    suspend fun getHabit(habitId: Int): HabitDbModel?

    @Transaction
    @Query("SELECT * FROM habits where id=:habitId")
    fun getHabitWithDaysFlow(habitId: Int): Flow<HabitWithDaysModel>

    @Transaction
    @Query("SELECT * FROM habits")
    fun getHabitsList(): Flow<List<HabitWithDaysModel>>
}