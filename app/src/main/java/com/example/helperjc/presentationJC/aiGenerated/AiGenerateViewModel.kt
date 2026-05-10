package com.example.helperjc.presentationJC.aiGenerated


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.ai.usecases.GeneratePlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AiGenerateState(
    val goal: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AiGenerateViewModel @Inject constructor(
    private val generatePlanUseCase: GeneratePlanUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AiGenerateState())
    val state = _state.asStateFlow()

    fun onGoalChange(goal: String) {
        _state.update { it.copy(goal = goal, error = null) }
    }

    fun onGenerateClick() {
        if (_state.value.goal.isBlank()) {
            _state.update { it.copy(error = "Опишите вашу цель") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            generatePlanUseCase(_state.value.goal)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}