package com.example.helperjc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plandetails.usecases.GetListPlanDetailsUseCase
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plans.usecases.DeletePlanUseCase
import com.example.helperjc.presentation.states.HelperState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HelperViewModel @Inject constructor(
    private val getListPlanDetailsUseCase: GetListPlanDetailsUseCase,
    private val deletePlanUseCase: DeletePlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<HelperState>(HelperState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getListPlanDetailsUseCase().collect {
                _state.value = HelperState.DataLoaded(it)
            }
        }
    }

    fun deletePlan(planDetails: PlanDetails) {
        val plan = planDetails.plan
        viewModelScope.launch {
            deletePlanUseCase(plan.id)
        }
    }
}