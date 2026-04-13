package com.example.helperjc.presentationJC.addEditPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.enums.PlanColor
import com.example.helperjc.parseToString
import com.example.helperjc.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddPlanState(
    val title: String = "",
    val endTime: String? = null,
    val color: PlanColor = PlanColor.Default
)

@HiltViewModel
class AddPlanViewModel @Inject constructor(
    val addEditPlanUseCase: AddEditPlanUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddPlanState())
    val state = _state.asStateFlow()


    fun onSavePlanClick(): Boolean {
        if (_state.value.title.isBlank()) {
//            showSnackbarMessage(R.string.add_edit_task_screen_blank_title_error_message)
            return false
        } else {
            viewModelScope.launch {
                addEditPlanUseCase(_state.value.toDomain())
            }
            return true
        }
    }


    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onEndTimeChange(endTime: LocalDateTime) {
        _state.update { it.copy(endTime = endTime.parseToString()) }
    }

    fun onEndTimeDelete() {
        _state.update { it.copy(endTime = null) }
    }

    fun onColorChanged(color: PlanColor) {
        _state.update { it.copy(color = color) }
    }
}


