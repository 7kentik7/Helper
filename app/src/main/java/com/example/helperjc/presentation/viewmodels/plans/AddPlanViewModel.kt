package com.example.helperjc.presentation.viewmodels.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.R

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.enums.PlanColor
import com.example.helperjc.presentation.states.plans.AddPlanState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class AddPlanViewModel @Inject constructor(
    val addEditPlanUseCase: AddEditPlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<AddPlanState>(AddPlanState.Data())
    val state = _state.asStateFlow()


    fun onSaveButtonClick() {
        (_state.value as? AddPlanState.Data)?.let {
            if (it.title.isNotBlank()) {
                val plan = Plan(
                    title = it.title.trim(),
                    endTime = it.endTime,
                    color = it.color
                )
                viewModelScope.launch {
                    addEditPlanUseCase(plan)
                    _state.value = AddPlanState.Success
                }
            } else {
                val oldState = _state.value
                _state.value = AddPlanState.Error(R.string.title_cannot_be_empty)
                _state.value = oldState
            }
        }
    }


    fun onTitleChange(title: String) {
        (_state.value as? AddPlanState.Data)?.let {
            _state.value = it.copy(
                title = title
            )
        }
    }

    fun onEndTimeChange(endTime: LocalDateTime) {
        (_state.value as? AddPlanState.Data)?.let {
            _state.value = it.copy(
                endTime = endTime
            )
        }
    }

    fun onEndTimeDelete() {
        (_state.value as? AddPlanState.Data)?.let {
            _state.value = it.copy(
                endTime = null
            )
        }
    }

    fun onColorChanged(color: PlanColor) {
        (_state.value as? AddPlanState.Data)?.let {
            _state.value = it.copy(color = color)
        }
    }
}

