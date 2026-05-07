package com.example.helperjc.presentationJC.plandetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plandetails.usecases.GetPlanDetailsUseCase
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.DeleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanDetailsState(
    val planId: Int = Plan.UNDEFINED_ID,
    val currencyList: List<Task> = listOf()
)

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val getPlanDetailsUseCase: GetPlanDetailsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addEditTaskUseCase: AddEditTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(PlanDetailsState())
    val state = _state.asStateFlow()

    private val planId: String? = savedStateHandle["planId"]

    init {
        loadData(planId)
    }

    fun loadData(planId: String?) {
        viewModelScope.launch {
            planId?.let { planId ->
                getPlanDetailsUseCase(planId.toInt()).collect { (_, tasks, _) ->
                    val sortedList = tasks.sortedWith(
                        compareBy<Task> {
                            it.isCompleted
                        }.thenByDescending {
                            it.priority
                        }
                    )
                    _state.value =
                        PlanDetailsState(planId = planId.toInt(), currencyList = sortedList)
                }
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
                isCompleted = !task.isCompleted,
                planId = _state.value.planId
            )
            addEditTaskUseCase(newTask)
        }
    }
}