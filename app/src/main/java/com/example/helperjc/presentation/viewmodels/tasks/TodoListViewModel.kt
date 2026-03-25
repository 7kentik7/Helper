package com.example.helperjc.presentation.viewmodels.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.DeleteTaskUseCase
import com.example.helperjc.domain.tasks.usecases.GetTasksListUseCase
import com.example.helperjc.presentation.states.tasks.TaskListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTasksListUseCase: GetTasksListUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addEditTaskUseCase: AddEditTaskUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<TaskListState>(TaskListState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getTasksListUseCase().collect { tasks ->
                val sortedList = tasks
                    .sortedWith(
                        compareBy<Task> {
                            !it.isActive
                        }.thenByDescending {
                            it.priority
                        }
                    )
                _state.value = TaskListState.DataLoaded(sortedList)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task.id)
        }
    }

    fun changeEnableState(task: Task) {
        viewModelScope.launch {
            val newTask = task.copy(
                isActive = !task.isActive
            )
            addEditTaskUseCase(newTask)
        }
    }
}