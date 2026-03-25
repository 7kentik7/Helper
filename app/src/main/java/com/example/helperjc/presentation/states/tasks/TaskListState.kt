package com.example.helperjc.presentation.states.tasks

import com.example.helperjc.domain.tasks.Task

sealed class TaskListState {
    data object Initial : TaskListState()
    data class DataLoaded(
        val currencyList: List<Task>
    ) : TaskListState()
}