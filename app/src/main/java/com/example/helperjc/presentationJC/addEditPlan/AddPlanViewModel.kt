package com.example.helperjc.presentationJC.addEditPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.enums.PlanColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddPlanState(
    val title: String = "",
    val endTime: LocalDateTime? = null,
    val color: PlanColor = PlanColor.Default
)

@HiltViewModel
class AddPlanViewModel @Inject constructor(
    val addEditPlanUseCase: AddEditPlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddPlanState())
    val state = _state.asStateFlow()


    fun onSaveButtonClick(): Boolean {
        _state.value.let {
            if (it.title.isNotBlank()) {
                val plan = Plan(
                    title = it.title.trim(), endTime = it.endTime, color = it.color
                )
                viewModelScope.launch {
                    addEditPlanUseCase(plan)
                }
                return true
            } else {
                return false
            }
        }
    }


    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onEndTimeChange(endTime: LocalDateTime) {
        _state.update { it.copy(endTime = endTime) }
    }

    fun onEndTimeDelete() {
        _state.update { it.copy(endTime = null) }
    }

    fun onColorChanged(color: PlanColor) {
        _state.update { it.copy(color = color) }
    }
}


