package com.example.helperjc.presentationJC.addEditTask


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.GetTaskUseCase
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.utils.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTaskState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val planId: String? = null,
    val showTitleError: Boolean = false
)

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addEditTaskUseCase: AddEditTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val taskId: String? = savedStateHandle["taskId"]
    private val planId: String? = savedStateHandle["planId"]
    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent = _errorEvent.asSharedFlow()
    private val _state = MutableStateFlow(AddTaskState())
    val state = _state.asStateFlow()

    init {
        loadData(taskId, planId)
    }

    fun loadData(taskId: String?, planId: String?) {
        viewModelScope.launch {
            _state.value = taskId?.let { taskId ->
                val task = getTaskUseCase(taskId.toInt())
                AddTaskState(
                    id = taskId,
                    title = task?.title ?: "",
                    description = task?.description ?: "",
                    isCompleted = task?.isCompleted ?: true,
                    priority = task?.priority ?: TaskPriority.MEDIUM,
                    planId = task?.planId?.toString() ?: planId
                )
            } ?: AddTaskState(planId = planId)
        }
    }

    fun onSaveButtonClick(): Boolean {
        if (_state.value.title.isBlank()) {
            _state.update { it.copy(showTitleError = true) }
            viewModelScope.launch {
                _errorEvent.emit("Название не может быть пустым")
            }
            return false
        }
        _state.update { it.copy(showTitleError = false) }
        viewModelScope.launch {
            addEditTaskUseCase(_state.value.toDomain())
        }
        return true
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

