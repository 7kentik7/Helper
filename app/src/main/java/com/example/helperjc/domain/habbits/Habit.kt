package com.example.helperjc.domain.habbits

import androidx.compose.ui.graphics.Color
import java.time.YearMonth


data class Habit(
    val id: Int = UNDEFINED_ID,
    val title: String = "",
    val color: Color = Color.Gray
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}