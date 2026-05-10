package com.example.helperjc.data.network

data class PlanDto(
    val title: String,
    val color: String,
    val tasks: List<TaskDto>
) {
}