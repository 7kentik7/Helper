package com.example.helperjc.presentation.viewmodels.tasks


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.R
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.GetTaskUseCase
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentation.states.tasks.AddTaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AddTaskState>(AddTaskState.Initial)
    val state = _state.asStateFlow()
    fun loadData(taskId: Int?) {
        viewModelScope.launch {
            _state.value = taskId?.let { taskId ->
                val task = getTaskUseCase(taskId)
                AddTaskState.DataLoaded(
                    id = taskId,
                    title = task?.title ?: "",
                    description = task?.description ?: "",
                    isActive = task?.isActive ?: true,
                    priority = task?.priority ?: TaskPriority.MEDIUM
                )
            } ?: AddTaskState.DataLoaded()
        }
    }

    fun onSaveButtonClick() {
        (_state.value as? AddTaskState.DataLoaded)?.let {
            if (it.title.isNotBlank()) {
                val task = Task(
                    id = it.id,
                    title = it.title.trim(),
                    description = it.description.trim(),
                    isActive = it.isActive,
                    priority = it.priority,
                )
                viewModelScope.launch {
                    addEditTaskUseCase(task)
                    _state.value = AddTaskState.Success
                }

            } else {
                val oldState = _state.value
                _state.value = AddTaskState.Error(R.string.title_cannot_be_empty)
                _state.value = oldState
            }
        }
    }

    fun onTitleChange(title: String) {
        (_state.value as? AddTaskState.DataLoaded)?.let {
            _state.value = it.copy(title = title)
        }
    }

    fun onDescriptionChange(description: String) {
        (_state.value as? AddTaskState.DataLoaded)?.let {
            _state.value = it.copy(description = description)
        }
    }

    fun onPriorityChange(priority: TaskPriority) {
        (_state.value as? AddTaskState.DataLoaded)?.let {
            _state.value = it.copy(priority = priority)
        }
    }
}
