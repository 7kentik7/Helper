package com.example.helperjc.presentation.viewmodels.plans

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plandetails.usecases.GetPlanDetailsUseCase
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.domain.tasks.usecases.AddEditTaskUseCase
import com.example.helperjc.domain.tasks.usecases.DeleteTaskUseCase
import com.example.helperjc.presentation.states.plans.PlanDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlanDetailsViewModel @Inject constructor(
    private val getPlanDetailsUseCase: GetPlanDetailsUseCase,
    private val addEditPlanUseCase: AddEditPlanUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val addEditTaskUseCase: AddEditTaskUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<PlanDetailsState>(PlanDetailsState.Initial)
    val state = _state.asStateFlow()


    fun loadData(planId: Int?) {
        viewModelScope.launch {
            planId?.let { planId ->
                getPlanDetailsUseCase(planId).collect { (plan, tasks, progress) ->
                    val sortedList = tasks.sortedWith(
                        compareBy<Task> {
                            !it.isCompleted
                        }.thenByDescending {
                            it.priority
                        }
                    )
                    _state.value = PlanDetailsState.DataLoaded(
                        plan = plan,
                        tasksList = sortedList,
                        progress = progress
                    )
                    Log.d("State", "${_state.value}")
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task.id)
        }
    }

    fun changeEnableState(task: Task, planId: Int) {
        (_state.value as? PlanDetailsState.DataLoaded)?.let {
            viewModelScope.launch {
                val newTask = task.copy(
                    isCompleted = !task.isCompleted,
                    planId = planId
                )
                addEditTaskUseCase(newTask)
            }
            Log.d("State", "${_state.value}")
        }
    }
}