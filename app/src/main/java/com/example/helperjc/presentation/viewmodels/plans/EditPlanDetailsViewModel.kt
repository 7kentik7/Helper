package com.example.helperjc.presentation.viewmodels.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.R
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.domain.plans.usecases.GetPlanUseCase
import com.example.helperjc.enums.PlanColor
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.presentation.states.plans.EditPlanDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class EditPlanDetailsViewModel @Inject constructor(
    private val addEditPlanUseCase: AddEditPlanUseCase,
    private val getPlanUseCase: GetPlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<EditPlanDetailsState>(EditPlanDetailsState.Initial)
    val state = _state.asStateFlow()

    fun loadData(planId: Int?) {
        viewModelScope.launch {
            _state.value = planId?.let { planId ->
                val plan = getPlanUseCase(planId)
                EditPlanDetailsState.DataLoaded(
                    id = planId,
                    title = plan?.title ?: "",
                    color = plan?.color ?: PlanColor.Default,
                    endTime = plan?.endTime,
                    startTime = plan?.startTime ?: LocalDateTime.now(),
                    repeatAt = plan?.repeatAt ?: PlanRepeatType.NONE
                )
            } ?: EditPlanDetailsState.DataLoaded()
        }
    }

    fun onSaveButtonClick() {
        (_state.value as? EditPlanDetailsState.DataLoaded)?.let {
            if (it.title.isNotBlank()) {
                val plan = Plan(
                    id = it.id,
                    title = it.title.trim(),
                    endTime = it.endTime,
                    color = it.color,
                    startTime = it.startTime
                )
                viewModelScope.launch {
                    addEditPlanUseCase(plan)
                    _state.value = EditPlanDetailsState.Success
                }

            } else {
                val oldState = _state.value
                _state.value = EditPlanDetailsState.Error(R.string.title_cannot_be_empty)
                _state.value = oldState
            }
        }
    }

    fun onColorChanged(color: PlanColor) {
        (_state.value as? EditPlanDetailsState.DataLoaded)?.let {
            _state.value = it.copy(color = color)
        }
    }

    fun onEndTimeChange(endTime: LocalDateTime) {
        (_state.value as? EditPlanDetailsState.DataLoaded)?.let {
            _state.value = it.copy(endTime = endTime)
        }
    }

    fun onEndTimeDelete() {
        (_state.value as? EditPlanDetailsState.DataLoaded)?.let {
            _state.value = it.copy(endTime = null)
        }
    }

    fun onTitleChange(title: String) {
        (_state.value as? EditPlanDetailsState.DataLoaded)?.let {
            _state.value = it.copy(title = title)
        }
    }

}