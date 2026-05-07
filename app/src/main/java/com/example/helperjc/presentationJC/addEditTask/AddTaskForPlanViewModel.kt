package com.example.helperjc.presentationJC.addEditTask

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.R
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.GetTaskUseCase
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentation.states.tasks.AddTaskForPlanState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTaskForPlanViewModel @Inject constructor(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AddTaskForPlanState>(AddTaskForPlanState.Initial)
    val state = _state.asStateFlow()
    fun loadData(taskId: Int?) {
        viewModelScope.launch {
            _state.value = taskId?.let { taskId ->
                val task = getTaskUseCase(taskId)
                AddTaskForPlanState.DataLoaded(
                    id = taskId,
                    title = task?.title ?: "",
                    description = task?.description ?: "",
                    isActive = task?.isCompleted ?: true,
                    priority = task?.priority ?: TaskPriority.MEDIUM
                )
            } ?: AddTaskForPlanState.DataLoaded()
        }
    }

    fun onSaveButtonClick(planId: Int?) {
        (_state.value as? AddTaskForPlanState.DataLoaded)?.let {
            if (it.title.isNotBlank()) {
                val task = Task(
                    id = it.id,
                    title = it.title.trim(),
                    description = it.description.trim(),
                    isCompleted = it.isActive,
                    priority = it.priority,
                    planId = planId ?: Plan.UNDEFINED_ID
                )
                viewModelScope.launch {
                    Log.d("DEBUG", "Task planId = ${task.planId}")
                    addEditTaskUseCase(task)
                    _state.value = AddTaskForPlanState.Success
                }

            } else {
                val oldState = _state.value
                _state.value = AddTaskForPlanState.Error(R.string.title_cannot_be_empty)
                _state.value = oldState
            }
        }
    }

    fun onTitleChange(title: String) {
        (_state.value as? AddTaskForPlanState.DataLoaded)?.let {
            _state.value = it.copy(title = title)
        }
    }

    fun onDescriptionChange(description: String) {
        (_state.value as? AddTaskForPlanState.DataLoaded)?.let {
            _state.value = it.copy(description = description)
        }
    }

    fun onPriorityChange(priority: TaskPriority) {
        (_state.value as? AddTaskForPlanState.DataLoaded)?.let {
            _state.value = it.copy(priority = priority)
        }
    }
}