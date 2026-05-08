package com.example.helperjc.presentationJC.addEditPlan

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.plans.usecases.AddEditPlanUseCase
import com.example.helperjc.domain.plans.usecases.GetPlanUseCase
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.notifications.NotificationScheduler
import com.example.helperjc.utils.longToStringFormattedDate
import com.example.helperjc.utils.parseToLocalDateTime
import com.example.helperjc.utils.parseToString
import com.example.helperjc.utils.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddPlanState(
    val planId: String? = null,
    val title: String = "",
    val endTime: String? = null,
    val color: Color = Color.Gray,
    val repeatType: PlanRepeatType = PlanRepeatType.NONE,
    val startTime: LocalDateTime? = LocalDateTime.now()
)

@HiltViewModel
class AddPlanViewModel @Inject constructor(
    val addEditPlanUseCase: AddEditPlanUseCase,
    val getPlanUseCase: GetPlanUseCase,
    val notificationScheduler: NotificationScheduler,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val planId: String? = savedStateHandle["planId"]
    private val _state = MutableStateFlow(AddPlanState())
    val state = _state.asStateFlow()

    init {
        loadData(planId)
    }

    fun loadData(planId: String?) {
        viewModelScope.launch {
            _state.value = planId?.let { planId ->
                val plan = getPlanUseCase(planId.toInt())
                AddPlanState(
                    planId = planId,
                    title = plan?.title ?: "",
                    endTime = plan?.endTime?.parseToString(),
                    color = plan?.color ?: Color.Gray,
                    startTime = plan?.startTime
                )
            } ?: AddPlanState()
        }
    }

    fun onSavePlanClick(): Boolean {
        if (_state.value.title.isBlank()) return false
        viewModelScope.launch {
            val plan = _state.value.toDomain()
            addEditPlanUseCase(plan)
            val endTime = _state.value.endTime?.parseToLocalDateTime()
            val repeatType = _state.value.repeatType
            if (endTime != null && repeatType != PlanRepeatType.NONE) {
                notificationScheduler.schedule(
                    planId = plan.id,
                    title = plan.title,
                    endTime = endTime,
                    repeatType = repeatType
                )
            }
//            notificationScheduler.scheduleTest(
//                planId = plan.id,
//                title = plan.title,
//                repeatType = _state.value.repeatType
//            )
        }
        return true
    }

    fun onRepeatTypeChange(repeatType: PlanRepeatType) {
        _state.update { it.copy(repeatType = repeatType) }
    }

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onEndTimeChange(endTime: Long) {
        _state.update { it.copy(endTime = endTime.longToStringFormattedDate()) }
    }

    fun onEndTimeDelete() {
        _state.update { it.copy(endTime = null) }
    }

    fun onColorChanged(color: Color) {
        _state.update { it.copy(color = color) }
    }
}


