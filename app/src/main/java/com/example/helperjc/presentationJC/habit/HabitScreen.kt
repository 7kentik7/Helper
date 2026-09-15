package com.example.helperjc.presentationJC.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HabitScreen() {
    Scaffold(
    ) { innerPading ->

        Row() {
            repeat(4) {
                HabitDay()
            }
        }
    }
}

@Preview
@Composable
private fun Habit() {
    Card(shape = RectangleShape) {
        Row() {
            repeat(7) {
                Column() {
                    repeat(4) {
                        HabitDay()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HabitDay(
) {
    Box(
        Modifier
            .size(12.dp)
            .padding(1.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .border(color = MaterialTheme.colorScheme.outline, width = 1.dp)
    )
}





