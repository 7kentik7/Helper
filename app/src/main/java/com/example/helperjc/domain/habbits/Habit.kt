package com.example.helperjc.domain.habbits

import java.time.YearMonth


data class Habit(
    val id: Int = UNDEFINED_ID,
    val title: String = "",
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}