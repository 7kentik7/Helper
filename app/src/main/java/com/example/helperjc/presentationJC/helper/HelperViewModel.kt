package com.example.helperjc.presentationJC.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plandetails.usecases.GetListPlanDetailsUseCase
import com.example.helperjc.domain.plans.usecases.DeletePlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HelperState(val planDetailsList: List<PlanDetails> = emptyList())

@HiltViewModel
class HelperViewModel @Inject constructor(
    private val getListPlanDetailsUseCase: GetListPlanDetailsUseCase,
    private val deletePlanUseCase: DeletePlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HelperState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getListPlanDetailsUseCase().collect {
                _state.value = HelperState(it)
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