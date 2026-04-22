package com.example.helperjc.presentationJC.addEditTask


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.GetTaskUseCase
import com.example.helperjc.enums.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTaskState(
    val id: Int = Task.UNDEFINED_ID,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM
)

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddTaskState())
    val state = _state.asStateFlow()

    fun loadData(taskId: Int?) {
        viewModelScope.launch {
            _state.value = taskId?.let { taskId ->
                val task = getTaskUseCase(taskId)
                AddTaskState(
                    id = taskId,
                    title = task?.title ?: "",
                    description = task?.description ?: "",
                    isCompleted = task?.isCompleted ?: true,
                    priority = task?.priority ?: TaskPriority.MEDIUM
                )
            } ?: AddTaskState()
        }
    }

    fun onSaveButtonClick(): Boolean {
        _state.value.let {

            if (it.title.isNotBlank()) {
                val task = Task(
                    id = it.id,
                    title = it.title.trim(),
                    description = it.description.trim(),
                    isCompleted = it.isCompleted,
                    priority = it.priority,
                )
                viewModelScope.launch {
                    addEditTaskUseCase(task)
                }
                return true

            } else {
                val oldState = _state.value
                _state.value = oldState
                return false
            }
        }
    }

    fun onTitleChange(title: String) {
        _state.update {
            it.copy(title = title)
        }
    }

    fun onDescriptionChange(description: String) {
        _state.update {
            it.copy(description = description)
        }
    }

    fun onPriorityChange(priority: TaskPriority) {
        _state.update {
            it.copy(priority = priority)
        }
    }
}

