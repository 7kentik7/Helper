package com.example.helperjc.presentation.states

import com.example.helperjc.domain.plandetails.PlanDetails

sealed class HelperState {
    data object Initial : HelperState()
    data class DataLoaded(val currentList: List<PlanDetails>) : HelperState()
}