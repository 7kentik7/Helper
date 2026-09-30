package com.example.helperjc.presentationJC.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.domain.habbits.usecases.DeleteHabitUseCase
import com.example.helperjc.domain.habbits.usecases.GetHabitWithDaysListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitState(
    val habitList: List<HabitWithDays> = emptyList(),
    val period: HabitPeriod = HabitPeriod.WEEK
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitWithDaysListUseCase: GetHabitWithDaysListUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HabitState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getHabitWithDaysListUseCase().collect {
                _state.value = HabitState(habitList = it)
            }
        }
    }
}
